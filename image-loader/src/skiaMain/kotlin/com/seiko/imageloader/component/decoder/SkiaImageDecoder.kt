package com.seiko.imageloader.component.decoder

import com.seiko.imageloader.request.Options
import com.seiko.imageloader.request.SourceResult
import okio.BufferedSource
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Image

class SkiaImageDecoder(
    private val channel: BufferedSource,
) : Decoder {

    // add this if jvm ./gradlew failed
    // @Suppress("TYPE_MISMATCH")
    override suspend fun decode(): DecodeResult {
        val image = Image.makeFromEncoded(channel.readByteArray())
        return DecodeImageResult(
            image = Bitmap.makeFromImage(image),
        )
    }

    class Factory : Decoder.Factory {
        override suspend fun create(source: SourceResult, options: Options): Decoder {
            return SkiaImageDecoder(source.channel)
        }
    }
}
