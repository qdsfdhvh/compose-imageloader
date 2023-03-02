package com.seiko.imageloader.intercept

import com.seiko.imageloader.component.ComponentRegistry
import com.seiko.imageloader.component.fetcher.FetchResult
import com.seiko.imageloader.model.DataSource
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.option.Options

class FetchInterceptor : Interceptor {

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val request = chain.request
        val options = chain.options
        return runCatching {
            fetch(chain.components, request, options)
        }.fold(
            onSuccess = {
                it.toImageResult(request)
            },
            onFailure = {
                ImageResult.Error(
                    request = request,
                    error = it,
                )
            },
        )
    }

    private fun FetchResult.toImageResult(request: ImageRequest) = when (this) {
        is FetchResult.Source -> ImageResult.Source(
            request = request,
            source = source,
            dataSource = DataSource.Engine,
            extra = extra,
        )
        is FetchResult.Bitmap -> ImageResult.Bitmap(
            request = request,
            bitmap = bitmap,
        )
        is FetchResult.Image -> ImageResult.Image(
            request = request,
            image = image,
        )
        is FetchResult.Painter -> ImageResult.Painter(
            request = request,
            painter = painter,
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
