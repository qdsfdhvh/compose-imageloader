package com.seiko.imageloader

import androidx.compose.runtime.Immutable
import com.seiko.imageloader.component.ComponentRegistry
import com.seiko.imageloader.intercept.EngineInterceptor
import com.seiko.imageloader.intercept.Interceptor
import com.seiko.imageloader.intercept.RealInterceptorChain
import com.seiko.imageloader.request.ErrorResult
import com.seiko.imageloader.request.ImageRequest
import com.seiko.imageloader.request.ImageResult
import com.seiko.imageloader.size.Size
import kotlinx.coroutines.CancellationException

@Immutable
interface ImageLoader {
    val components: ComponentRegistry
    suspend fun execute(request: ImageRequest): ImageResult
}

@Immutable
class RealImageLoader(
    override val components: ComponentRegistry,
    interceptors: List<Interceptor>,
) : ImageLoader {

    private val interceptors = listOf(
        EngineInterceptor(this)
    ) + interceptors

    override suspend fun execute(request: ImageRequest): ImageResult {
        return executeMain(request)
    }

    private suspend fun executeMain(initialRequest: ImageRequest): ImageResult {
        val request = initialRequest.newBuilder().build()
        // val size = request.sizeResolver?.size() ?: Size.ORIGINAL
        try {
            return RealInterceptorChain(
                initialRequest = initialRequest,
                request = initialRequest,
                interceptors = interceptors,
                index = 0,
                // size = size,
                isPlaceholderCached = false,
            ).proceed(request)
        } catch (throwable: Throwable) {
            if (throwable is CancellationException) {
                throw throwable
            } else {
                return ErrorResult(
                    request = initialRequest,
                    error = throwable,
                )
            }
        }
    }
}
