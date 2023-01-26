package com.seiko.imageloader.intercept

import com.seiko.imageloader.ImageLoaderConfig
import com.seiko.imageloader.component.ComponentRegistry
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.model.ImageResult

internal class RealInterceptorChain(
    override val initialRequest: ImageRequest,
    override val request: ImageRequest,
    override val config: ImageLoaderConfig,
    private val index: Int,
    private val interceptors: List<Interceptor>,
    override val components: ComponentRegistry,
) : Interceptor.Chain {

    constructor(
        initialRequest: ImageRequest,
        config: ImageLoaderConfig,
    ) : this(
        initialRequest = initialRequest,
        config = config,
        request = initialRequest,
        index = 0,
        interceptors = initialRequest.interceptors?.plus(config.engine.interceptors)
            ?: config.engine.interceptors,
        components = initialRequest.components?.merge(config.engine.componentRegistry)
            ?: config.engine.componentRegistry,
    )

    private fun copy(index: Int, request: ImageRequest) = RealInterceptorChain(
        initialRequest = initialRequest,
        config = config,
        request = request,
        index = index,
        interceptors = interceptors,
        components = components,
    )

    override suspend fun proceed(request: ImageRequest): ImageResult {
        check(index < interceptors.size)
        val interceptor = interceptors[index]
        val next = copy(index = index + 1, request = request)
        return interceptor.intercept(next)
    }
}
