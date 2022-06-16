package com.seiko.imageloader.intercept

import com.seiko.imageloader.component.ComponentRegistry
import com.seiko.imageloader.component.fetcher.FetchPainterResult
import com.seiko.imageloader.component.fetcher.FetchResult
import com.seiko.imageloader.component.fetcher.FetchSourceResult
import com.seiko.imageloader.request.ComposePainterResult
import com.seiko.imageloader.request.ImageResult
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.request.SourceResult

class EngineInterceptor : Interceptor {

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val (request, options, components) = chain
        return when (val fetchResult = fetch(components, request.data, options)) {
            is FetchSourceResult -> SourceResult(
                request = request,
                channel = fetchResult.source,
                mimeType = fetchResult.mimeType,
                metadata = fetchResult.metadata,
            )
            is FetchPainterResult -> ComposePainterResult(
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
            val (fetcher, index) = components.fetch(mappedData, options, searchIndex)
            searchIndex = index + 1

            val result = fetcher.fetch()
            if (result != null) {
                fetchResult = result
                break
            }
        }
        return fetchResult
    }
}
