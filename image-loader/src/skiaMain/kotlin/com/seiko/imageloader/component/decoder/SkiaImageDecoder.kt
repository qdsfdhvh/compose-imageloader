package com.seiko.imageloader.component.decoder

import com.seiko.imageloader.option.Options
import okio.BufferedSource
import okio.use
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Image

class SkiaImageDecoder private constructor(
    private val source: BufferedSource,
) : Decoder {

    override suspend fun decode(): DecodeResult {
        val image = source.use {
            Image.makeFromEncoded(it.readByteArray())
        }
        return DecodeResult.Bitmap(
            bitmap = Bitmap.makeFromImage(image),
        )
    }

    class Factory : Decoder.Factory {
        override suspend fun create(source: DecodeSource, options: Options): Decoder {
            return SkiaImageDecoder(source.source)
        }
    }
}
