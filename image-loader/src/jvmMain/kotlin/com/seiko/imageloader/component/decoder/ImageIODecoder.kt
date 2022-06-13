package com.seiko.imageloader.component.decoder

import androidx.compose.ui.graphics.toPainter
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.component.fetcher.SourceResult
import com.seiko.imageloader.request.Options
import io.ktor.utils.io.jvm.javaio.toInputStream
import javax.imageio.ImageIO

class ImageIODecoder(
    private val source: SourceResult,
) : Decoder {

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun decode(): DecoderResult {
        val painter = ImageIO.read(source.source.toInputStream()).toPainter()
        return PainterResult(
            painter = painter,
        )
    }

    class Factory : Decoder.Factory {
        override fun create(
            result: SourceResult,
            options: Options,
            imageLoader: ImageLoader
        ): Decoder {
            return ImageIODecoder(result)
        }
    }
}