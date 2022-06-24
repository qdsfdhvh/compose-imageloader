package com.seiko.imageloader.component.decoder

import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.request.SourceResult
import io.ktor.util.toByteArray
import io.ktor.utils.io.ByteReadChannel
import org.jetbrains.skia.Image

class SkiaImageDecoder(
    private val channel: ByteReadChannel,
) : Decoder {

    override suspend fun decode(): DecoderResult {
        val image = Image.makeFromEncoded(channel.toByteArray())
        return DecodePainterResult(
            painter = BitmapPainter(image.toComposeImageBitmap()),
        )
    }

    class Factory : Decoder.Factory {
        override suspend fun create(source: SourceResult, options: Options): Decoder {
            return SkiaImageDecoder(source.channel)
        }
    }
}
