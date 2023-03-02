package com.seiko.imageloader.intercept

import com.seiko.imageloader.cache.CachePolicy
import com.seiko.imageloader.component.ComponentRegistry
import com.seiko.imageloader.component.decoder.DecodeResult
import com.seiko.imageloader.component.decoder.DecodeSource
import com.seiko.imageloader.model.DataSource
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.option.Options

class DecodeInterceptor : Interceptor {

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        return proceed(chain, chain.request)
    }

    private suspend fun proceed(chain: Interceptor.Chain, request: ImageRequest): ImageResult {
        val options = chain.options
        return when (val result = chain.proceed(request)) {
            is ImageResult.Source -> {
                runCatching {
                    decode(chain.components, result, options)
                }.fold(
                    onSuccess = { it.toImageResult(request) },
                    onFailure = {
                        if (options.retryIfDiskDecodeError && result.dataSource == DataSource.Disk) {
                            val noDiskCacheRequest = request.newBuilder {
                                options {
                                    retryIfDiskDecodeError = false
                                    diskCachePolicy = when (options.diskCachePolicy) {
                                        CachePolicy.ENABLED,
                                        CachePolicy.READ_ONLY,
                                        -> CachePolicy.WRITE_ONLY
                                        else -> options.diskCachePolicy
                                    }
                                }
                            }
                            proceed(chain, noDiskCacheRequest)
                        } else {
                            ImageResult.Error(
                                request = request,
                                error = it,
                            )
                        }
                    },
                )
            }

            else -> result
        }
    }

    private fun DecodeResult.toImageResult(request: ImageRequest) = when (this) {
        is DecodeResult.Bitmap -> ImageResult.Bitmap(
            request = request,
            bitmap = bitmap,
        )
        is DecodeResult.Image -> ImageResult.Image(
            request = request,
            image = image,
        )
        is DecodeResult.Painter -> ImageResult.Painter(
            request = request,
            painter = painter,
        )
    }

    private suspend fun decode(
        components: ComponentRegistry,
        source: DecodeSource,
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
