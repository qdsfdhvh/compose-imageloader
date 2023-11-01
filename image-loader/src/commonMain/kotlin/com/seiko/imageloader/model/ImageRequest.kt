package com.seiko.imageloader.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter
import com.seiko.imageloader.Poko
import com.seiko.imageloader.component.ComponentRegistry
import com.seiko.imageloader.component.ComponentRegistryBuilder
import com.seiko.imageloader.intercept.Interceptor
import com.seiko.imageloader.option.OptionsBuilder
import com.seiko.imageloader.option.Scale
import com.seiko.imageloader.option.SizeResolver

@Immutable
class ImageRequest internal constructor(
    val data: Any,
    val extra: ExtraData,
    val sizeResolver: SizeResolver,
    val placeholderPainter: (@Composable () -> Painter)?,
    val errorPainter: (@Composable () -> Painter)?,
    val skipEvent: Boolean,
    internal val optionsBuilders: List<OptionsBuilder.() -> Unit>,
    internal val components: ComponentRegistry?,
    internal val interceptors: List<Interceptor>?,
) {
    @Deprecated("", ReplaceWith("ImageRequest(request) {}"))
    fun newBuilder(block: ImageRequestBuilder.() -> Unit) = ImageRequest(this, block)

    override fun equals(other: Any?): Boolean {
        return other is ImageRequest &&
            data == other.data &&
            extra == other.extra &&
            sizeResolver == other.sizeResolver &&
            placeholderPainter == other.placeholderPainter &&
            errorPainter == other.errorPainter &&
            skipEvent == other.skipEvent &&
            optionsBuilders == other.optionsBuilders &&
            components == other.components &&
            interceptors == other.interceptors
    }

    override fun hashCode(): Int {
        var result = data.hashCode()
        result = 31 * result + extra.hashCode()
        result = 31 * result + sizeResolver.hashCode()
        result = 31 * result + placeholderPainter.hashCode()
        result = 31 * result + errorPainter.hashCode()
        result = 31 * result + skipEvent.hashCode()
        result = 31 * result + optionsBuilders.hashCode()
        result = 31 * result + components.hashCode()
        result = 31 * result + interceptors.hashCode()
        return result
    }
}

class ImageRequestBuilder internal constructor() {

    private var data: Any? = null
    private var sizeResolver: SizeResolver = SizeResolver.Unspecified
    private val optionsBuilders: MutableList<OptionsBuilder.() -> Unit> = mutableListOf()
    private var extraData: ExtraData? = null
    private var placeholderPainter: (@Composable () -> Painter)? = null
    private var errorPainter: (@Composable () -> Painter)? = null
    private var componentBuilder: ComponentRegistryBuilder? = null
    private var interceptors: MutableList<Interceptor>? = null
    var skipEvent: Boolean = false

    fun takeFrom(
        request: ImageRequest,
        clearOptions: Boolean = false,
    ) {
        data = request.data
        if (clearOptions) {
            optionsBuilders.clear()
        }
        optionsBuilders.addAll(request.optionsBuilders)
        extraData = request.extra
        placeholderPainter = request.placeholderPainter
        errorPainter = request.errorPainter
        componentBuilder = request.components?.let { ComponentRegistryBuilder(it) }
        interceptors = request.interceptors?.toMutableList()
        skipEvent = request.skipEvent
    }

    fun data(data: Any?) {
        this.data = data
    }

    fun size(sizeResolver: SizeResolver) {
        this.sizeResolver = sizeResolver
    }

    fun scale(scale: Scale) {
        optionsBuilders.add {
            this.scale = scale
        }
    }

    fun options(block: OptionsBuilder.() -> Unit) {
        optionsBuilders.add(block)
    }

    fun components(builder: ComponentRegistryBuilder.() -> Unit) {
        (componentBuilder ?: ComponentRegistryBuilder().also { componentBuilder = it }).run(builder)
    }

    fun addInterceptor(interceptor: Interceptor) {
        (interceptors ?: mutableListOf<Interceptor>().also { interceptors = it }).add(interceptor)
    }

    fun extra(builder: ExtraDataBuilder.() -> Unit) {
        extraData = extraData
            ?.takeUnless { it.isEmpty() }
            ?.toMutableMap()
            ?.apply(builder)
            ?: extraData(builder)
    }

    fun placeholderPainter(loader: @Composable () -> Painter) {
        placeholderPainter = loader
    }

    fun errorPainter(loader: @Composable () -> Painter) {
        errorPainter = loader
    }

    internal fun build() = ImageRequest(
        data = data ?: NullRequestData,
        sizeResolver = sizeResolver,
        optionsBuilders = optionsBuilders,
        extra = extraData ?: EmptyExtraData,
        placeholderPainter = placeholderPainter,
        errorPainter = errorPainter,
        skipEvent = skipEvent,
        components = componentBuilder?.build(),
        interceptors = interceptors,
    )
}

fun ImageRequest(block: ImageRequestBuilder.() -> Unit) =
    ImageRequestBuilder().apply(block).build()

inline fun ImageRequest(
    request: ImageRequest,
    crossinline block: ImageRequestBuilder.() -> Unit,
) = ImageRequest {
    takeFrom(request)
    block.invoke(this)
}

inline fun ImageRequest(
    data: Any,
    crossinline block: ImageRequestBuilder.() -> Unit,
) = ImageRequest {
    data(data)
    block.invoke(this)
}

inline fun ImageRequest(request: ImageRequest) = request

inline fun ImageRequest(data: Any) = ImageRequest {
    data(data)
}
