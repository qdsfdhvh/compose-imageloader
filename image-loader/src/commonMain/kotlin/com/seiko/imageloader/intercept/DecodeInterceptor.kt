package com.seiko.imageloader.intercept

import com.seiko.imageloader.cache.CachePolicy
import com.seiko.imageloader.component.ComponentRegistry
import com.seiko.imageloader.component.decoder.DecodeImageResult
import com.seiko.imageloader.component.decoder.DecodePainterResult
import com.seiko.imageloader.component.decoder.DecodeResult
import com.seiko.imageloader.request.ComposeImageResult
import com.seiko.imageloader.request.ComposePainterResult
import com.seiko.imageloader.request.DataSource
import com.seiko.imageloader.request.ErrorResult
import com.seiko.imageloader.request.ImageRequest
import com.seiko.imageloader.request.ImageResult
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.request.SourceResult
import io.ktor.utils.io.CancellationException

class DecodeInterceptor : Interceptor {

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        return proceed(chain, chain.request)
    }

    private suspend fun proceed(chain: Interceptor.Chain, request: ImageRequest): ImageResult {
        return when (val result = chain.proceed(request)) {
            is SourceResult -> {
                runCatching {
                    decode(chain.components, result, chain.options)
                }.fold(
                    onSuccess = { it.toImageResult(request) },
                    onFailure = {
                        if (chain.options.retryIfDiskDecodeError && result.dataSource == DataSource.Disk) {
                            val noDiskCacheRequest = chain.request.newBuilder()
                                .options(chain.options.copy(
                                    retryIfDiskDecodeError = false,
                                    diskCachePolicy = CachePolicy.WRITE_ONLY,
                                ))
                                .build()
                            proceed(chain, noDiskCacheRequest)
                        } else {
                            ErrorResult(
                                request = request,
                                error = it,
                            )
                        }
                    }
                )
            }
            else -> result
        }
    }

    private fun DecodeResult.toImageResult(request: ImageRequest) = when (this) {
        is DecodeImageResult -> ComposeImageResult(
            request = request,
            image = image,
        )
        is DecodePainterResult -> ComposePainterResult(
            request = request,
            painter = painter,
        )
    }

    private suspend fun decode(
        components: ComponentRegistry,
        source: SourceResult,
        options: Options,
    ): DecodeResult {
        var searchIndex = 0
        val decodeResult: DecodeResult
        while (true) {
            val (decoder, index) = components.decode(source, options, searchIndex)
            searchIndex = index + 1

            val result = decoder.decode()
            if (result != null) {
                decodeResult = result
                break
            }
        }
        return decodeResult
    }
}
