package com.seiko.imageloader.intercept

import com.seiko.imageloader.request.ImageRequest
import com.seiko.imageloader.request.ImageResult
import com.seiko.imageloader.request.NullRequestData
import com.seiko.imageloader.size.Size


internal data class RealInterceptorChain(
    val initialRequest: ImageRequest,
    val interceptors: List<Interceptor>,
    val index: Int,
    override val request: ImageRequest,
    // override val size: Size,
    // val eventListener: EventListener,
    val isPlaceholderCached: Boolean,
) : Interceptor.Chain {

    // override fun withSize(size: Size) = copy(size = size)

    override suspend fun proceed(request: ImageRequest): ImageResult {
        if (index > 0) checkRequest(request, interceptors[index - 1])
        val interceptor = interceptors[index]
        val next = copy(index = index + 1, request = request)
        val result = interceptor.intercept(next)
        checkRequest(result.request, interceptor)
        return result
    }

    private fun checkRequest(request: ImageRequest, interceptor: Interceptor) {
        // check(request.context === initialRequest.context) {
        //     "Interceptor '$interceptor' cannot modify the request's context."
        // }
        check(request.data !== NullRequestData) {
            "Interceptor '$interceptor' cannot set the request's data to null."
        }
        // check(request.target === initialRequest.target) {
        //     "Interceptor '$interceptor' cannot modify the request's target."
        // }
        // check(request.lifecycle === initialRequest.lifecycle) {
        //     "Interceptor '$interceptor' cannot modify the request's lifecycle."
        // }
        // check(request.sizeResolver === initialRequest.sizeResolver) {
        //     "Interceptor '$interceptor' cannot modify the request's size resolver. " +
        //         "Use `Interceptor.Chain.withSize` instead."
        // }
    }
}