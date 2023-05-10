package com.seiko.imageloader.component.fetcher

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.toImage
import com.seiko.imageloader.toPainter
import dev.icerock.moko.resources.AssetResource
import dev.icerock.moko.resources.ColorResource
import dev.icerock.moko.resources.FileResource
import dev.icerock.moko.resources.ImageResource
import okio.buffer
import okio.source

actual class MokoResourceFetcher {

    class Factory(private val context: Context) : Fetcher.Factory {
        override fun create(data: Any, options: Options): Fetcher? {
            return when (data) {
                is AssetResource -> AssetFetcher(context, data)
                is ColorResource -> ColorFetcher(context, data)
                is FileResource -> FileFetcher(context, data)
                is ImageResource -> ImageFetcher(context, data)
                else -> null
            }
        }
    }

    private class AssetFetcher(
        private val context: Context,
        private val resource: AssetResource,
    ) : Fetcher {
        override suspend fun fetch(): FetchResult {
            return FetchResult.Source(
                source = resource.getInputStream(context).source().buffer(),
            )
        }
    }

    private class ColorFetcher(
        private val context: Context,
        private val resource: ColorResource,
    ) : Fetcher {
        override suspend fun fetch(): FetchResult {
            return FetchResult.Painter(
                painter = ColorPainter(Color(resource.getColor(context)))
            )
        }
    }

    private class FileFetcher(
        private val context: Context,
        private val resource: FileResource,
    ) : Fetcher {
        override suspend fun fetch(): FetchResult {
            return FetchResult.Source(
                source = context.resources.openRawResource(resource.rawResId).source().buffer(),
            )
        }
    }

    private class ImageFetcher(
        private val context: Context,
        private val resource: ImageResource,
    ) : Fetcher {
        override suspend fun fetch(): FetchResult {
            return FetchResult.Painter(
                painter = resource.getDrawable(context)!!.toImage().toPainter()
            )
        }
    }
}