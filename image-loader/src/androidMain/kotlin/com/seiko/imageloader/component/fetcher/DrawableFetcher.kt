package com.seiko.imageloader.component.fetcher

import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.request.Options

class DrawableFetcher(
    private val data: Drawable,
) : Fetcher {
    override suspend fun fetch(): FetchResult {
        return FetchPainterResult(
            image = data.toBitmap().asImageBitmap()
        )
    }

    class Factory : Fetcher.Factory {
        override fun create(data: Any, options: Options, imageLoader: ImageLoader): Fetcher? {
            if (data !is Drawable) return null
            return DrawableFetcher(data)
        }
    }
}