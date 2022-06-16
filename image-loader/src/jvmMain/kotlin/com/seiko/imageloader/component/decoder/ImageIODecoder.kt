package com.seiko.imageloader.component.decoder

import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.request.SourceResult
import io.ktor.utils.io.jvm.javaio.toInputStream
import javax.imageio.ImageIO
import kotlinx.coroutines.runInterruptible

class ImageIODecoder(
    private val source: SourceResult,
) : Decoder {

    override suspend fun decode(): DecoderResult = runInterruptible {
        val image = ImageIO.read(source.channel.toInputStream())
        DecodePainterResult(
            painter = BitmapPainter(image.toComposeImageBitmap()),
        )
    }

    class Factory : Decoder.Factory {
        override suspend fun create(source: SourceResult, options: Options): Decoder {
            return ImageIODecoder(source)
        }
    }
}
