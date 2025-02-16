package com.seiko.imageloader.intercept

import com.seiko.imageloader.component.ComponentRegistry
import com.seiko.imageloader.component.fetcher.FetchResult
import com.seiko.imageloader.model.ImageEvent
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.option.Options

class FetchInterceptor : Interceptor {

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val request = chain.request
        val options = chain.options
        if (!request.skipEvent) {
            chain.emit(ImageEvent.StartWithFetch)
        }
        return fetch(chain.components, request, options).toImageResult()
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
    is FetchResult.OfSource -> ImageResult.OfSource(
        imageSource = imageSource,
        imageSourceFrom = imageSourceFrom,
        extra = extra,
    )
    is FetchResult.OfBitmap -> ImageResult.OfBitmap(
        bitmap = bitmap,
    )
    is FetchResult.OfImage -> ImageResult.OfImage(
        image = image,
    )
    is FetchResult.OfPainter -> ImageResult.OfPainter(
        painter = painter,
    )
}
