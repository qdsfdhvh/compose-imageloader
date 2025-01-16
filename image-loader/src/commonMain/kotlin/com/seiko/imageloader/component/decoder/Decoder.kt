package com.seiko.imageloader.component.decoder

import androidx.compose.ui.graphics.painter.Painter
import com.seiko.imageloader.Bitmap
import com.seiko.imageloader.Image
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.option.Options
import dev.drewhamilton.poko.Poko

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
    @Poko
    class OfBitmap(
        val bitmap: Bitmap,
        val isSampled: Boolean = false,
    ) : DecodeResult

    @Poko
    class OfImage(val image: Image) : DecodeResult

    @Poko
    class OfPainter(val painter: Painter) : DecodeResult
}
