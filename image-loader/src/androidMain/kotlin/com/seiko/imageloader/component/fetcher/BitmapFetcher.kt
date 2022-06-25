package com.seiko.imageloader.component.fetcher

import android.graphics.Bitmap
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.toPainter

class BitmapFetcher(
    private val data: Bitmap,
) : Fetcher {
    override suspend fun fetch(): FetchResult {
        return FetchPainterResult(
            painter = data.toPainter()
        )
    }

    class Factory : Fetcher.Factory {
        override fun create(data: Any, options: Options): Fetcher? {
            if (data !is Bitmap) return null
            return BitmapFetcher(data)
        }
    }
}
