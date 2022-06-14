package com.seiko.imageloader.intercept

import com.seiko.imageloader.component.ComponentRegistry
import com.seiko.imageloader.request.ImageRequest
import com.seiko.imageloader.request.ImageResult
import com.seiko.imageloader.request.NullRequestData
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.size.Size


internal class RealInterceptorChain(
    override val initialRequest: ImageRequest,
    private val interceptors: List<Interceptor>,
    val index: Int,
    override val request: ImageRequest,
    override val options: Options,
    override val components: ComponentRegistry,
) : Interceptor.Chain {

    private fun copy(
        index: Int = this.index,
        request: ImageRequest = this.request,
    ) = RealInterceptorChain(
        initialRequest = initialRequest,
        interceptors = interceptors,
        index = index,
        request = request,
        options = options,
        components = components,
    )

    override suspend fun proceed(request: ImageRequest): ImageResult {
        check(index < interceptors.size)

        val interceptor = interceptors[index]
        checkRequest(request, interceptor)

        val next = copy(index = index + 1, request = request)
        val result = interceptor.intercept(next)
        checkRequest(result.request, interceptor)

        return result
    }

    private fun checkRequest(request: ImageRequest, interceptor: Interceptor) {
        check(request.data !== NullRequestData) {
            "Interceptor '$interceptor' cannot set the request's data to null."
        }
        // check(request.target === initialRequest.target) {
        //     "Interceptor '$interceptor' cannot modify the request's target."
        // }
        // check(request.sizeResolver === initialRequest.sizeResolver) {
        //     "Interceptor '$interceptor' cannot modify the request's size resolver. " +
        //         "Use `Interceptor.Chain.withSize` instead."
        // }
    }
}