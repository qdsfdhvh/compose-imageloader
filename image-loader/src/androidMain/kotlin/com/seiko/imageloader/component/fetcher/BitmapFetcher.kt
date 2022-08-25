package com.seiko.imageloader.component.fetcher

import android.graphics.Bitmap
import com.seiko.imageloader.request.Options

class BitmapFetcher(
    private val data: Bitmap,
) : Fetcher {
    override suspend fun fetch(): FetchResult {
        return FetchImageResult(
            image = data
        )
    }

    class Factory : Fetcher.Factory {
        override fun create(data: Any, options: Options): Fetcher? {
            if (data !is Bitmap) return null
            return BitmapFetcher(data)
        }
    }
}
