package com.seiko.imageloader

import androidx.compose.runtime.Immutable
import com.seiko.imageloader.cache.disk.DiskCache
import com.seiko.imageloader.cache.memory.MemoryCache
import com.seiko.imageloader.component.ComponentRegistry
import com.seiko.imageloader.intercept.DecodeInterceptor
import com.seiko.imageloader.intercept.DiskCacheInterceptor
import com.seiko.imageloader.intercept.EngineInterceptor
import com.seiko.imageloader.intercept.Interceptor
import com.seiko.imageloader.intercept.MappedInterceptor
import com.seiko.imageloader.intercept.MemoryCacheInterceptor
import com.seiko.imageloader.intercept.RealInterceptorChain
import com.seiko.imageloader.request.ErrorResult
import com.seiko.imageloader.request.ImageRequest
import com.seiko.imageloader.request.ImageResult
import com.seiko.imageloader.request.Options
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext

@Immutable
interface ImageLoader {
    val imageScope: CoroutineScope
    suspend fun execute(request: ImageRequest): ImageResult
}

@Immutable
class RealImageLoader(
    private val components: ComponentRegistry,
    private val options: Options,
    private val requestDispatcher: CoroutineDispatcher,
    override val imageScope: CoroutineScope,
    interceptors: List<Interceptor>,
    memoryCache: Lazy<MemoryCache>?,
    diskCache: Lazy<DiskCache>?,
) : ImageLoader {

    private val interceptors = interceptors + listOfNotNull(
        MappedInterceptor(),
        memoryCache?.let { MemoryCacheInterceptor(it) },
        DecodeInterceptor(),
        diskCache?.let { DiskCacheInterceptor(it) },
        EngineInterceptor(),
    )

    override suspend fun execute(request: ImageRequest): ImageResult {
        return withContext(requestDispatcher) {
            executeMain(request)
        }
    }

    private suspend fun executeMain(initialRequest: ImageRequest): ImageResult {
        val request = initialRequest.newBuilder().build()
        return try {
            RealInterceptorChain(
                initialRequest = request,
                initialOptions = request.options ?: options,
                interceptors = interceptors,
                index = 0,
                components = components,
                request = request,
            ).proceed(request)
        } catch (throwable: Throwable) {
            if (throwable is CancellationException) {
                throw throwable
            } else {
                ErrorResult(
                    request = request,
                    error = throwable,
                )
            }
        }
    }
}
