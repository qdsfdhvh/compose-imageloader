package com.seiko.imageloader

import androidx.compose.runtime.Immutable
import com.seiko.imageloader.cache.disk.DiskCache
import com.seiko.imageloader.cache.memory.MemoryCache
import com.seiko.imageloader.component.ComponentRegistry
import com.seiko.imageloader.intercept.DecodeInterceptor
import com.seiko.imageloader.intercept.DiskCacheInterceptor
import com.seiko.imageloader.intercept.FetchInterceptor
import com.seiko.imageloader.intercept.Interceptor
import com.seiko.imageloader.intercept.MappedInterceptor
import com.seiko.imageloader.intercept.MemoryCacheInterceptor
import com.seiko.imageloader.intercept.RealInterceptorChain
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.option.Options
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
        FetchInterceptor(),
    )

    override suspend fun execute(request: ImageRequest): ImageResult {
        return withContext(requestDispatcher) {
            executeMain(request)
        }
    }

    private suspend fun executeMain(request: ImageRequest): ImageResult {
        return try {
            RealInterceptorChain(
                initialRequest = request,
                initialOptions = options,
                interceptors = request.interceptors.orEmpty() + interceptors,
                index = 0,
                components = request.components?.merge(components) ?: components,
                request = request,
            ).proceed(request)
        } catch (throwable: Throwable) {
            if (throwable is CancellationException) {
                throw throwable
            } else {
                ImageResult.Error(
                    request = request,
                    error = throwable,
                )
            }
        }
    }
}
