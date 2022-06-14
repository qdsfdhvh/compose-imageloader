package com.seiko.imageloader.component.decoder

import androidx.compose.ui.graphics.toComposeImageBitmap
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.request.SourceResult
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.coroutines.runInterruptible
import javax.imageio.ImageIO

class ImageIODecoder(
    private val source: SourceResult,
) : Decoder {

    override suspend fun decode(): DecoderResult = runInterruptible {
        val image = ImageIO.read(source.source.toInputStream())
        DecodeImageResult(
            image = image.toComposeImageBitmap(),
        )
    }

    class Factory : Decoder.Factory {
        override fun create(
            source: SourceResult,
            options: Options,
        ): Decoder {
            return ImageIODecoder(source)
        }
    }
}
