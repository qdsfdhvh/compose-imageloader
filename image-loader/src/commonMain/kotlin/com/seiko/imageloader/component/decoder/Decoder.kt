package com.seiko.imageloader.component.decoder

import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.option.Options

typealias DecodeSource = ImageResult.Source

interface Decoder {
    suspend fun decode(): DecodeResult?
    interface Factory {
        suspend fun create(source: DecodeSource, options: Options): Decoder?
    }
}

sealed interface DecodeResult {
    data class Bitmap(
        val bitmap: com.seiko.imageloader.Bitmap,
    ) : DecodeResult

    data class Image(
        val image: com.seiko.imageloader.Image,
    ) : DecodeResult

    data class Painter(
        val painter: androidx.compose.ui.graphics.painter.Painter,
    ) : DecodeResult
}
