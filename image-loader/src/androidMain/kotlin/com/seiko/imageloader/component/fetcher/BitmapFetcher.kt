package com.seiko.imageloader.component.fetcher

import android.graphics.Bitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.seiko.imageloader.request.Options

class BitmapFetcher(
    private val data: Bitmap,
) : Fetcher {
    override suspend fun fetch(): FetchResult {
        return FetchPainterResult(
            image = data.asImageBitmap()
        )
    }

    class Factory : Fetcher.Factory {
        override fun create(data: Any, options: Options): Fetcher? {
            if (data !is Bitmap) return null
            return BitmapFetcher(data)
        }
    }
}
