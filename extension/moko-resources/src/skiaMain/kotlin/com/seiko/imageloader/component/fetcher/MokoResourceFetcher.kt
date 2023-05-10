package com.seiko.imageloader.component.fetcher

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import com.seiko.imageloader.Image
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.toPainter
import dev.icerock.moko.resources.AssetResource
import dev.icerock.moko.resources.ColorResource
import dev.icerock.moko.resources.FileResource
import dev.icerock.moko.resources.ImageResource
import okio.BufferedSource

actual class MokoResourceFetcher {

    class Factory() : Fetcher.Factory {
        override fun create(data: Any, options: Options): Fetcher? {
            return when (data) {
                is AssetResource -> AssetFetcher(data)
                is ColorResource -> ColorFetcher(data)
                is FileResource -> FileFetcher(data)
                is ImageResource -> ImageFetcher(data)
                else -> null
            }
        }
    }

    private class AssetFetcher(
        private val resource: AssetResource,
    ) : Fetcher {
        override suspend fun fetch(): FetchResult {
            return FetchResult.Source(
                source = resource.toSource(),
            )
        }
    }

    private class ColorFetcher(
        private val resource: ColorResource,
    ) : Fetcher {
        override suspend fun fetch(): FetchResult {
            return FetchResult.Painter(
                painter = ColorPainter(resource.toColor())
            )
        }
    }

    private class FileFetcher(
        private val resource: FileResource,
    ) : Fetcher {
        override suspend fun fetch(): FetchResult {
            return FetchResult.Source(
                source = resource.toSource(),
            )
        }
    }

    private class ImageFetcher(
        private val resource: ImageResource,
    ) : Fetcher {
        override suspend fun fetch(): FetchResult {
            return FetchResult.Painter(
                painter = resource.toImage().toPainter()
            )
        }
    }
}

internal expect fun AssetResource.toSource(): BufferedSource

internal expect fun ColorResource.toColor(): Color

internal expect fun FileResource.toSource(): BufferedSource

internal expect fun ImageResource.toImage(): Image
