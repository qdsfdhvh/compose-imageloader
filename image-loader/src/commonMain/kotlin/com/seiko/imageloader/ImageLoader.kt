package com.seiko.imageloader

import androidx.compose.runtime.Immutable
import com.seiko.imageloader.intercept.RealInterceptorChain
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.model.ImageRequestEvent
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.util.ioDispatcher
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

@Immutable
interface ImageLoader {
    val config: ImageLoaderConfig
    suspend fun execute(request: ImageRequest): ImageResult
}

fun ImageLoader(
    requestCoroutineContext: CoroutineContext = ioDispatcher,
    block: ImageLoaderConfigBuilder.() -> Unit,
): ImageLoader = RealImageLoader(
    requestCoroutineContext = requestCoroutineContext,
    config = ImageLoaderConfig(block),
)

@Immutable
private class RealImageLoader(
    private val requestCoroutineContext: CoroutineContext,
    override val config: ImageLoaderConfig,
) : ImageLoader {
    override suspend fun execute(request: ImageRequest): ImageResult {
        return withContext(requestCoroutineContext) {
            runCatching {
                request.call(ImageRequestEvent.Prepare)
                RealInterceptorChain(
                    initialRequest = request,
                    config = config,
                ).proceed(request)
            }.getOrElse {
                ImageResult.Error(
                    request = request,
                    error = it,
                )
            }
        }
    }
}
