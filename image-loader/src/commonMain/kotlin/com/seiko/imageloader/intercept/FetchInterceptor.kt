package com.seiko.imageloader.intercept

import com.seiko.imageloader.component.ComponentRegistry
import com.seiko.imageloader.component.fetcher.FetchResult
import com.seiko.imageloader.model.DataSource
import com.seiko.imageloader.model.ImageEvent
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.option.Options

class FetchInterceptor : Interceptor {

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val request = chain.request
        val options = chain.options
        chain.emit(ImageEvent.StartWithFetch)
        return runCatching {
            fetch(chain.components, request, options)
        }.fold(
            onSuccess = {
                it.toImageResult()
            },
            onFailure = {
                ImageResult.Error(it)
            },
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

private fun FetchResult.toImageResult() = when (this) {
    is FetchResult.Source -> ImageResult.Source(
        source = source,
        dataSource = DataSource.Engine,
        extra = extra,
    )
    is FetchResult.Bitmap -> ImageResult.Bitmap(
        bitmap = bitmap,
    )
    is FetchResult.Image -> ImageResult.Image(
        image = image,
    )
    is FetchResult.Painter -> ImageResult.Painter(
        painter = painter,
    )
}
