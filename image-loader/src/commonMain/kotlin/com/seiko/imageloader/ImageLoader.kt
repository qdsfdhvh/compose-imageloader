package com.seiko.imageloader

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.isSpecified
import com.seiko.imageloader.intercept.InterceptorChainImpl
import com.seiko.imageloader.model.ImageAction
import com.seiko.imageloader.model.ImageEvent
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.util.ioDispatcher
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.coroutines.CoroutineContext

@Immutable
interface ImageLoader {
    val config: ImageLoaderConfig

    fun async(request: ImageRequest): Flow<ImageAction>

    companion object
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
    override fun async(request: ImageRequest) = flow {
        if (!request.skipEvent) {
            emit(ImageEvent.Start)
        }
        val initialSize = request.sizeResolver.size()
        val options = Options(config.defaultOptions) {
            if (initialSize.isSpecified) {
                size = initialSize
            }
        }
        val chain = InterceptorChainImpl(
            initialRequest = request,
            initialOptions = options,
            config = config,
            flowCollector = this,
        )
        emit(chain.proceed(request))
    }.catch {
        if (it !is CancellationException) {
            emit(ImageResult.OfError(it))
        }
    }.flowOn(requestCoroutineContext)
}
