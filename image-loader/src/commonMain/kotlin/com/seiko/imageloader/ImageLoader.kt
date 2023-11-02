package com.seiko.imageloader

import androidx.compose.runtime.Immutable
import com.seiko.imageloader.intercept.InterceptorChainImpl
import com.seiko.imageloader.model.ImageAction
import com.seiko.imageloader.model.ImageEvent
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.util.ioDispatcher
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transformLatest
import kotlin.coroutines.CoroutineContext

@Immutable
interface ImageLoader {
    val config: ImageLoaderConfig

    fun async(requestFlow: Flow<ImageRequest>): Flow<ImageAction>

    fun async(request: ImageRequest): Flow<ImageAction> = async(flowOf(request))

    @Deprecated("", ReplaceWith("Use imageloader.async(request).filterIsInstance<ImageResult>().first()"))
    suspend fun execute(request: ImageRequest): ImageResult {
        return async(request).filterIsInstance<ImageResult>().first()
    }

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
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun async(requestFlow: Flow<ImageRequest>) = requestFlow
        .transformLatest { request ->
            if (!request.skipEvent) {
                emit(ImageEvent.Start)
            }
            val chain = InterceptorChainImpl(
                initialRequest = request,
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
