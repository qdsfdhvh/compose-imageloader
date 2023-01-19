package com.seiko.imageloader.component.fetcher

import android.graphics.Bitmap
import com.seiko.imageloader.option.Options

class BitmapFetcher(
    private val data: Bitmap,
) : Fetcher {
    override suspend fun fetch(): FetchResult {
        return FetchResult.Bitmap(
            bitmap = data
        )
    }

    class Factory : Fetcher.Factory {
        override fun create(data: Any, options: Options): Fetcher? {
            if (data !is Bitmap) return null
            return BitmapFetcher(data)
        }
    }
}
