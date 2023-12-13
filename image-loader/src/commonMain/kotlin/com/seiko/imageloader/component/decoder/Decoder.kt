package com.seiko.imageloader.component.decoder

import androidx.compose.ui.graphics.painter.Painter
import com.seiko.imageloader.Bitmap
import com.seiko.imageloader.Image
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.option.Options

typealias DecodeSource = ImageResult.OfSource

interface Decoder {
    suspend fun decode(): DecodeResult?
    fun interface Factory {
        fun create(source: DecodeSource, options: Options): Decoder?
    }
}

fun Decoder(block: () -> DecodeResult?) = object : Decoder {
    override suspend fun decode(): DecodeResult? = block()
}

sealed interface DecodeResult {
    class OfBitmap(val bitmap: Bitmap) : DecodeResult {
        override fun hashCode(): Int {
            return bitmap.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is OfBitmap) return false
            return bitmap == other.bitmap
        }
    }

    class OfImage(val image: Image) : DecodeResult {
        override fun hashCode(): Int {
            return image.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is OfImage) return false
            return image == other.image
        }
    }

    class OfPainter(val painter: Painter) : DecodeResult {
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
