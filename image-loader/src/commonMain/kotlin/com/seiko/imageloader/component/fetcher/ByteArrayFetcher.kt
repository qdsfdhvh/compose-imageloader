package com.seiko.imageloader.component.fetcher

import com.seiko.imageloader.model.ImageSourceFrom
import com.seiko.imageloader.model.toImageSource
import com.seiko.imageloader.option.Options

class ByteArrayFetcher(
    private val data: ByteArray,
) : Fetcher {
    override suspend fun fetch(): FetchResult {
        return FetchResult.OfSource(
            imageSource = data.toImageSource(),
            imageSourceFrom = ImageSourceFrom.Memory,
        )
    }

    class Factory : Fetcher.Factory {
        override fun create(data: Any, options: Options): Fetcher? {
            if (data !is ByteArray) return null
            return ByteArrayFetcher(data)
        }
    }
}
