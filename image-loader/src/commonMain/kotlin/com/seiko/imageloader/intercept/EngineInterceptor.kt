package com.seiko.imageloader.intercept

import com.seiko.imageloader.component.ComponentRegistry
import com.seiko.imageloader.component.fetcher.FetchImageResult
import com.seiko.imageloader.component.fetcher.FetchResult
import com.seiko.imageloader.component.fetcher.FetchSourceResult
import com.seiko.imageloader.request.ImageResult
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.request.SourceResult
import com.seiko.imageloader.request.SuccessResult
import io.github.aakira.napier.Napier

class EngineInterceptor : Interceptor {

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val (request, options, components) = chain
        return when (val fetchResult = fetch(components, request.data, options)) {
            is FetchSourceResult -> SourceResult(
                request = request,
                source = fetchResult.source,
                metadata = fetchResult.metadata,
            )
            is FetchImageResult -> SuccessResult(
                request = request,
                image = fetchResult.image,
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
