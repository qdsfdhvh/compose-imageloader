package com.seiko.imageloader.intercept

import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.component.ComponentRegistry
import com.seiko.imageloader.component.decoder.DecoderResult
import com.seiko.imageloader.component.decoder.PainterResult
import com.seiko.imageloader.component.fetcher.FetchPainterResult
import com.seiko.imageloader.component.fetcher.FetchResult
import com.seiko.imageloader.component.fetcher.SourceResult
import com.seiko.imageloader.request.ImageResult
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.request.SuccessResult

class EngineInterceptor(
    private val imageLoader: ImageLoader,
) : Interceptor {

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val request = chain.request
        val options = Options() // TODO()
        val components = imageLoader.components

        val data = request.data
        val mappedData = components.map(data, options)

        return when (val fetchResult = fetch(components, mappedData, options)) {
            is SourceResult -> {
                when (val decodeResult = decode(components, fetchResult, options)) {
                    is PainterResult -> SuccessResult(
                        request = request,
                        painter = decodeResult.painter,
                    )
                }
            }
            is FetchPainterResult -> SuccessResult(
                request = request,
                painter = fetchResult.painter,
            )
        }
    }

    private suspend fun fetch(
        components: ComponentRegistry,
        mappedData: Any,
        options: Options,
    ): FetchResult {
        var searchIndex = 0
        val fetchResult: FetchResult
        while (true) {
            val (fetcher, index) = components.fetch(mappedData, options, imageLoader, searchIndex)
            searchIndex = index + 1

            val result = fetcher.fetch()
            if (result != null) {
                fetchResult = result
                break
            }
        }
        return fetchResult
    }

    private suspend fun decode(
        components: ComponentRegistry,
        sourceResult: SourceResult,
        options: Options,
    ): DecoderResult {
        var searchIndex = 0
        val decodeResult: DecoderResult
        while (true) {
            val (decoder, index) = components.decode(sourceResult, options, imageLoader, searchIndex)
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