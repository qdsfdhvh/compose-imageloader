package com.seiko.imageloader.intercept

import com.seiko.imageloader.component.ComponentRegistry
import com.seiko.imageloader.request.ImageRequest
import com.seiko.imageloader.request.ImageResult
import com.seiko.imageloader.request.Options

internal class RealInterceptorChain(
    override val initialRequest: ImageRequest,
    override val initialOptions: Options,
    private val interceptors: List<Interceptor>,
    private val index: Int,
    override val request: ImageRequest,
    override val components: ComponentRegistry,
) : Interceptor.Chain {

    private fun copy(index: Int, request: ImageRequest) = RealInterceptorChain(
        initialRequest = initialRequest,
        initialOptions = initialOptions,
        interceptors = interceptors,
        index = index,
        request = request,
        components = components,
    )

    override suspend fun proceed(request: ImageRequest): ImageResult {
        check(index < interceptors.size)
        val interceptor = interceptors[index]
        val next = copy(index = index + 1, request = request)
        return interceptor.intercept(next)
    }
}
