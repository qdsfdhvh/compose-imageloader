package com.seiko.imageloader

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.isUnspecified
import com.seiko.imageloader.intercept.createInterceptorChain
import com.seiko.imageloader.model.ImageAction
import com.seiko.imageloader.model.ImageEvent
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.option.SizeResolver
import com.seiko.imageloader.util.ioDispatcher
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.CoroutineContext

@Immutable
interface ImageLoader {
    val config: ImageLoaderConfig

    fun async(request: ImageRequest): Flow<ImageAction>

    suspend fun execute(request: ImageRequest): ImageResult

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
        val chain = createInterceptorChain(
            initialRequest = request,
            initialOptions = getOptions(request),
            config = config,
            flowCollector = { emit(it) },
        )
        emit(chain.proceed(request))
    }.catch {
        if (it !is CancellationException) {
            emit(ImageResult.OfError(it))
        }
    }.flowOn(requestCoroutineContext)

    override suspend fun execute(request: ImageRequest): ImageResult {
        return withContext(requestCoroutineContext) {
            runCatching {
                val chain = createInterceptorChain(
                    initialRequest = request,
                    initialOptions = getOptions(request),
                    config = config,
                    flowCollector = null,
                )
                chain.proceed(request)
            }.fold(
                onSuccess = { it },
                onFailure = {
                    if (it !is CancellationException) {
                        ImageResult.OfError(it)
                    } else {
                        throw it
                    }
                },
            )
        }
    }

    private suspend fun getOptions(request: ImageRequest): Options {
        if (request.sizeResolver == SizeResolver.Unspecified) {
            return config.defaultOptions
        }

        val initialSize = withTimeoutOrNull(200) {
            request.sizeResolver.size()
        } ?: return config.defaultOptions

        if (initialSize.isUnspecified) {
            return config.defaultOptions
        }

        return Options(config.defaultOptions) {
            size = initialSize
        }
    }
}
