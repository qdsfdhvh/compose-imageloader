package com.seiko.imageloader.request

import androidx.compose.runtime.Immutable
import com.seiko.imageloader.component.ComponentRegistry
import com.seiko.imageloader.component.ComponentRegistryBuilder
import com.seiko.imageloader.intercept.Interceptor
import com.seiko.imageloader.size.Scale

@Immutable
class ImageRequest internal constructor(
    val data: Any,
    val scale: Scale?,
    val options: Options?,
    internal val components: ComponentRegistry?,
    internal val interceptors: List<Interceptor>?,
) {
    fun newBuilder() = ImageRequestBuilder(this)
}

class ImageRequestBuilder {

    private var data: Any?
    private var scale: Scale?
    private var options: Options?
    private var componentBuilder: ComponentRegistryBuilder? = null
    private var interceptors: MutableList<Interceptor>? = null

    constructor() {
        data = null
        scale = null
        options = null
    }

    constructor(request: ImageRequest) {
        data = request.data
        scale = request.scale
        options = request.options
        componentBuilder = request.components?.newBuilder()
        interceptors = request.interceptors?.toMutableList()
    }

    fun data(data: Any?) = apply {
        this.data = data
    }

    fun scale(scale: Scale) = apply {
        this.scale = scale
    }

    fun options(options: Options?) = apply {
        this.options = options
    }

    fun components(builder: ComponentRegistryBuilder.() -> Unit) = apply {
        (componentBuilder ?: ComponentRegistryBuilder().also { componentBuilder = it }).run(builder)
    }

    fun addInterceptor(interceptor: Interceptor) = apply {
        (interceptors ?: mutableListOf<Interceptor>().also { interceptors = it }).add(interceptor)
    }

    fun build() = ImageRequest(
        data = data ?: NullRequestData,
        scale = scale,
        options = options,
        components = componentBuilder?.build(),
        interceptors = interceptors,
    )
}
