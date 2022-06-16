package com.seiko.imageloader.intercept

import com.seiko.imageloader.component.ComponentRegistry
import com.seiko.imageloader.component.decoder.DecodeImageResult
import com.seiko.imageloader.component.decoder.DecodePainterResult
import com.seiko.imageloader.component.decoder.DecoderResult
import com.seiko.imageloader.request.ComposeImageResult
import com.seiko.imageloader.request.ComposePainterResult
import com.seiko.imageloader.request.ImageResult
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.request.SourceResult

class DecodeInterceptor : Interceptor {

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val (request, options, components) = chain
        return when (val result = chain.proceed(request)) {
            is SourceResult -> {
                when (val decodeResult = decode(components, result, options)) {
                    is DecodeImageResult -> ComposeImageResult(
                        request = request,
                        image = decodeResult.image,
                    )
                    is DecodePainterResult -> ComposePainterResult(
                        request = request,
                        painter = decodeResult.painter,
                    )
                }
            }
            else -> result
        }
    }

    private suspend fun decode(
        components: ComponentRegistry,
        source: SourceResult,
        options: Options,
    ): DecoderResult {
        var searchIndex = 0
        val decodeResult: DecoderResult
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
