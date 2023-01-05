package com.seiko.imageloader.intercept

import com.seiko.imageloader.component.ComponentRegistry
import com.seiko.imageloader.component.fetcher.FetchImageResult
import com.seiko.imageloader.component.fetcher.FetchPainterResult
import com.seiko.imageloader.component.fetcher.FetchResult
import com.seiko.imageloader.component.fetcher.FetchSourceResult
import com.seiko.imageloader.request.ComposeImageResult
import com.seiko.imageloader.request.ComposePainterResult
import com.seiko.imageloader.request.DataSource
import com.seiko.imageloader.request.ErrorResult
import com.seiko.imageloader.request.ImageRequest
import com.seiko.imageloader.request.ImageResult
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.request.SourceResult

class FetchInterceptor : Interceptor {

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        return runCatching {
            fetch(chain.components, chain.request, chain.options)
        }.fold(
            onSuccess = {
                it.toImageResult(chain.request)
            },
            onFailure = {
                ErrorResult(
                    request = chain.request,
                    error = it,
                )
            }
        )
    }

    private fun FetchResult.toImageResult(request: ImageRequest) = when (this) {
        is FetchSourceResult -> SourceResult(
            request = request,
            channel = source,
            dataSource = DataSource.Engine,
            mimeType = mimeType,
            metadata = metadata,
        )
        is FetchPainterResult -> ComposePainterResult(
            request = request,
            painter = painter,
        )
        is FetchImageResult -> ComposeImageResult(
            request = request,
            image = image
        )
    }

    private suspend fun fetch(
        components: ComponentRegistry,
        request: ImageRequest,
        options: Options,
    ): FetchResult {
        var searchIndex = 0
        val fetchResult: FetchResult
        while (true) {
            val (fetcher, index) = components.fetch(request.data, options, searchIndex)
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
