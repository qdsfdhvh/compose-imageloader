package com.seiko.imageloader.component.decoder

import androidx.compose.ui.graphics.toComposeImageBitmap
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.request.SourceResult
import io.ktor.util.toByteArray
import org.jetbrains.skia.Image

class SkiaImageDecoder(
    private val source: SourceResult,
) : Decoder {

    override suspend fun decode(): DecoderResult {
        val image = Image.makeFromEncoded(source.channel.toByteArray())
        return DecodeImageResult(
            image = image.toComposeImageBitmap(),
        )
    }

    class Factory : Decoder.Factory {
        override suspend fun create(source: SourceResult, options: Options): Decoder {
            return SkiaImageDecoder(source)
        }
    }
}
