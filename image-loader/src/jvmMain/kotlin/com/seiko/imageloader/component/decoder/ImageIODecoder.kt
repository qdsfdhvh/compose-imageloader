package com.seiko.imageloader.component.decoder

import androidx.compose.ui.graphics.toComposeImageBitmap
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.request.SourceResult
import io.ktor.utils.io.jvm.javaio.toInputStream
import javax.imageio.ImageIO
import kotlinx.coroutines.runInterruptible

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
            imageLoader: ImageLoader
        ): Decoder {
            return ImageIODecoder(source)
        }
    }
}