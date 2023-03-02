package com.seiko.imageloader.component.fetcher

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.toImage

class DrawableFetcher private constructor(
    private val data: Drawable,
) : Fetcher {
    override suspend fun fetch(): FetchResult {
        return if (data is BitmapDrawable) {
            FetchResult.Bitmap(
                bitmap = data.bitmap,
            )
        } else {
            FetchResult.Image(
                image = data.toImage(),
            )
        }
    }

    class Factory : Fetcher.Factory {
        override fun create(data: Any, options: Options): Fetcher? {
            if (data !is Drawable) return null
            return DrawableFetcher(data)
        }
    }
}
