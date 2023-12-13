package com.seiko.imageloader.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter
import com.seiko.imageloader.Bitmap
import com.seiko.imageloader.Image
import okio.BufferedSource

@Immutable
sealed interface ImageAction {
    sealed interface Loading : ImageAction
    sealed interface Success : ImageAction
    sealed interface Failure : ImageAction {
        val error: Throwable
    }
}

@Immutable
sealed interface ImageEvent : ImageAction.Loading {
    data object Start : ImageEvent
    data object StartWithMemory : ImageEvent
    data object StartWithDisk : ImageEvent
    data object StartWithFetch : ImageEvent
}

@Immutable
sealed interface ImageResult : ImageAction {

    @Immutable
    class OfBitmap(val bitmap: Bitmap) : ImageResult, ImageAction.Success {
        override fun hashCode(): Int {
            return bitmap.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is OfBitmap) return false
            return bitmap == other.bitmap
        }
    }

    @Immutable
    class OfImage(val image: Image) : ImageResult, ImageAction.Success {
        override fun hashCode(): Int {
            return image.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is OfImage) return false
            return image == other.image
        }
    }

    @Immutable
    class OfPainter(val painter: Painter) : ImageResult, ImageAction.Success {
        override fun hashCode(): Int {
            return painter.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is OfPainter) return false
            return painter == other.painter
        }
    }

    @Immutable
    class OfError(override val error: Throwable) : ImageResult, ImageAction.Failure {
        override fun hashCode(): Int {
            return error.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is OfError) return false
            return error == other.error
        }
    }

    @Immutable
    class OfSource(
        val source: BufferedSource,
        val dataSource: DataSource,
        val extra: ExtraData = EmptyExtraData,
    ) : ImageResult, ImageAction.Failure {
        override val error: Throwable
            get() = IllegalStateException("failure to decode image source")

        override fun hashCode(): Int {
            var result = source.hashCode()
            result = result * 31 + dataSource.hashCode()
            result = result * 31 + extra.hashCode()
            return result
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is OfSource) return false
            return source == other.source && dataSource == other.dataSource && extra == other.extra
        }
    }
}
