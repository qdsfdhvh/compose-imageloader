package com.seiko.imageloader.component.fetcher

import android.graphics.drawable.Drawable
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.util.toPainter

class DrawableFetcher(
    private val data: Drawable,
) : Fetcher {
    override suspend fun fetch(): FetchResult {
        return PainterResult(
            painter = data.toPainter()
        )
    }

    class Factory : Fetcher.Factory {
        override fun create(data: Any, options: Options, imageLoader: ImageLoader): Fetcher? {
            if (data !is Drawable) return null
            return DrawableFetcher(data)
        }
    }
}