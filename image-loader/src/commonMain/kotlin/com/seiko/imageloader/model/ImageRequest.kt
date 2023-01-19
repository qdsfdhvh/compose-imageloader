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
    internal val components: ComponentRegistry?,
    internal val interceptors: List<Interceptor>?,
) {
    fun newBuilder() = ImageRequestBuilder(this)
}

class ImageRequestBuilder {

    private var data: Any?
    private val optionsBuilders: MutableList<Options.() -> Unit>
    private var componentBuilder: ComponentRegistryBuilder? = null
    private var interceptors: MutableList<Interceptor>? = null

    constructor() {
        data = null
        optionsBuilders = mutableListOf()
    }

    constructor(request: ImageRequest) {
        data = request.data
        optionsBuilders = request.optionsBuilders.toMutableList()
        componentBuilder = request.components?.newBuilder()
        interceptors = request.interceptors?.toMutableList()
    }

    fun data(data: Any?) = apply {
        this.data = data
    }

    fun size(sizeResolver: SizeResolver) = apply {
        optionsBuilders.add {
            this.sizeResolver = sizeResolver
        }
    }

    fun scale(scale: Scale) = apply {
        optionsBuilders.add {
            this.scale = scale
        }
    }

    fun options(block: Options.() -> Unit) = apply {
        optionsBuilders.add(block)
    }

    fun components(builder: ComponentRegistryBuilder.() -> Unit) = apply {
        (componentBuilder ?: ComponentRegistryBuilder().also { componentBuilder = it }).run(builder)
    }

    fun addInterceptor(interceptor: Interceptor) = apply {
        (interceptors ?: mutableListOf<Interceptor>().also { interceptors = it }).add(interceptor)
    }

    fun build() = ImageRequest(
        data = data ?: NullRequestData,
        optionsBuilders = optionsBuilders,
        components = componentBuilder?.build(),
        interceptors = interceptors,
    )
}
