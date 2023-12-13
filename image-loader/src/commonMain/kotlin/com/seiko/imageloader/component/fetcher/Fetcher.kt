package com.seiko.imageloader.component.fetcher

import androidx.compose.ui.graphics.painter.Painter
import com.seiko.imageloader.Bitmap
import com.seiko.imageloader.Image
import com.seiko.imageloader.model.EmptyExtraData
import com.seiko.imageloader.model.ExtraData
import com.seiko.imageloader.option.Options
import okio.BufferedSource

interface Fetcher {
    suspend fun fetch(): FetchResult?
    fun interface Factory {
        fun create(data: Any, options: Options): Fetcher?
    }
}

fun Fetcher(block: suspend () -> FetchResult?) = object : Fetcher {
    override suspend fun fetch(): FetchResult? = block()
}

sealed interface FetchResult {
    class OfSource(
        val source: BufferedSource,
        val extra: ExtraData = EmptyExtraData,
    ) : FetchResult {
        override fun hashCode(): Int {
            var result = source.hashCode()
            result = result * 31 + extra.hashCode()
            return result
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is OfSource) return false
            return source == other.source && extra == other.extra
        }
    }

    class OfBitmap(val bitmap: Bitmap) : FetchResult {
        override fun hashCode(): Int {
            return bitmap.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is OfBitmap) return false
            return bitmap == other.bitmap
        }
    }

    class OfImage(val image: Image) : FetchResult {
        override fun hashCode(): Int {
            return image.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is OfImage) return false
            return image == other.image
        }
    }

    class OfPainter(val painter: Painter) : FetchResult {
        override fun hashCode(): Int {
            return painter.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is OfPainter) return false
            return painter == other.painter
        }
    }
}
