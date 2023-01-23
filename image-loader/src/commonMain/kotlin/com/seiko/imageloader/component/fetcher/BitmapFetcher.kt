package com.seiko.imageloader.component.fetcher

import com.seiko.imageloader.Bitmap
import com.seiko.imageloader.option.Options

class BitmapFetcher private constructor(
    private val data: Bitmap,
) : Fetcher {
    override suspend fun fetch(): FetchResult {
        return FetchResult.Bitmap(
            bitmap = data,
        )
    }

    class Factory : Fetcher.Factory {
        override fun create(data: Any, options: Options): Fetcher? {
            if (data !is Bitmap) return null
            return BitmapFetcher(data)
        }
    }
}
