package com.seiko.imageloader.model

import androidx.compose.runtime.Immutable
import com.seiko.imageloader.component.ComponentRegistry
import com.seiko.imageloader.component.ComponentRegistryBuilder
import com.seiko.imageloader.intercept.Interceptor
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.option.Scale
import com.seiko.imageloader.option.SizeResolver

@Immutable
class ImageRequest internal constructor(
    val data: Any,
    val optionsBuilders: List<Options.() -> Unit>,
    val extra: ExtraData,
    internal val components: ComponentRegistry?,
    internal val interceptors: List<Interceptor>?,
    internal val eventListener: List<ImageRequestEventListener>?,
) {
    internal fun call(event: ImageRequestEvent) {
        eventListener?.forEach { it.invoke(event) }
    }

    fun newBuilder(block: ImageRequestBuilder.() -> Unit) =
        ImageRequestBuilder(this).apply(block).build()
}

class ImageRequestBuilder {

    private var data: Any?
    private var extraData: ExtraData?
    private val optionsBuilders: MutableList<Options.() -> Unit>
    private var componentBuilder: ComponentRegistryBuilder?
    private var interceptors: MutableList<Interceptor>?
    private var eventListener: MutableList<ImageRequestEventListener>?

    internal constructor() {
        data = null
        extraData = null
        optionsBuilders = mutableListOf()
        componentBuilder = null
        interceptors = null
        eventListener = null
    }

    internal constructor(request: ImageRequest) {
        data = request.data
        extraData = request.extra
        optionsBuilders = request.optionsBuilders.toMutableList()
        componentBuilder = request.components?.newBuilder()
        interceptors = request.interceptors?.toMutableList()
        eventListener = request.eventListener?.toMutableList()
    }

    fun data(data: Any?) {
        this.data = data
    }

    fun size(sizeResolver: SizeResolver) {
        optionsBuilders.add {
            this.sizeResolver = sizeResolver
        }
    }

    fun scale(scale: Scale) {
        optionsBuilders.add {
            this.scale = scale
        }
    }

    fun options(block: Options.() -> Unit) {
        optionsBuilders.add(block)
    }

    fun components(builder: ComponentRegistryBuilder.() -> Unit) {
        (componentBuilder ?: ComponentRegistryBuilder().also { componentBuilder = it }).run(builder)
    }

    fun addInterceptor(interceptor: Interceptor) {
        (interceptors ?: mutableListOf<Interceptor>().also { interceptors = it }).add(interceptor)
    }

    fun eventListener(listener: (ImageRequestEvent) -> Unit) {
        (eventListener ?: mutableListOf<ImageRequestEventListener>().also { eventListener = it }).add(listener)
    }

    fun extra(builder: ExtraDataBuilder.() -> Unit) {
        extraData = extraData
            ?.takeUnless { it.isEmpty() }
            ?.toMutableMap()
            ?.apply(builder)
            ?: extraData(builder)
    }

    fun build() = ImageRequest(
        data = data ?: NullRequestData,
        optionsBuilders = optionsBuilders,
        components = componentBuilder?.build(),
        interceptors = interceptors,
        extra = extraData ?: EmptyExtraData,
        eventListener = eventListener,
    )
}

fun ImageRequest(block: ImageRequestBuilder.() -> Unit) =
    ImageRequestBuilder().apply(block).build()

fun ImageRequest(data: Any) = ImageRequest {
    data(data)
}
