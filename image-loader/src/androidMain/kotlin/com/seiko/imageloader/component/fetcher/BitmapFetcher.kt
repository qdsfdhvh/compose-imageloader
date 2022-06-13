package com.seiko.imageloader.component.fetcher

import android.graphics.Bitmap
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.util.toPainter

class BitmapFetcher(
    private val data: Bitmap,
) : Fetcher {
    override suspend fun fetch(): FetchResult {
        return PainterResult(
            painter = data.toPainter()
        )
    }

    class Factory : Fetcher.Factory {
        override fun create(data: Any, options: Options, imageLoader: ImageLoader): Fetcher? {
            if (data !is Bitmap) return null
            return BitmapFetcher(data)
        }
    }
}