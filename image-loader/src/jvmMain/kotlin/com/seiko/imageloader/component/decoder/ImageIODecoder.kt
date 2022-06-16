package com.seiko.imageloader.component.decoder

import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.request.SourceResult
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.coroutines.runInterruptible
import javax.imageio.ImageIO

class ImageIODecoder(
    private val channel: ByteReadChannel,
) : Decoder {

    override suspend fun decode() = runInterruptible {
        val image = ImageIO.read(channel.toInputStream())
        DecodePainterResult(
            painter = BitmapPainter(image.toComposeImageBitmap()),
        )
    }

    class Factory : Decoder.Factory {
        override suspend fun create(source: SourceResult, options: Options): Decoder {
            return ImageIODecoder(source.channel)
        }
    }
}
