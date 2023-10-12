package com.seiko.imageloader.component.decoder

import com.seiko.imageloader.Poko
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.option.Options

typealias DecodeSource = ImageResult.Source

interface Decoder {
    suspend fun decode(): DecodeResult?
    fun interface Factory {
        fun create(source: DecodeSource, options: Options): Decoder?
    }
}

sealed interface DecodeResult {
    @Poko class Bitmap(
        val bitmap: com.seiko.imageloader.Bitmap,
    ) : DecodeResult

    @Poko class Image(
        val image: com.seiko.imageloader.Image,
    ) : DecodeResult

    @Poko class Painter(
        val painter: androidx.compose.ui.graphics.painter.Painter,
    ) : DecodeResult
}
