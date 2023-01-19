package com.seiko.imageloader.component.decoder

import androidx.compose.ui.graphics.toPainter
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.util.isGif
import kotlinx.coroutines.runInterruptible
import okio.BufferedSource
import javax.imageio.ImageIO

class ImageIODecoder(
    private val source: BufferedSource,
) : Decoder {

    override suspend fun decode(): DecodeResult {
        val image = runInterruptible {
            ImageIO.read(source.inputStream())
        }
        return DecodeResult.Painter(
            painter = image.toPainter(),
        )
    }

    class Factory : Decoder.Factory {
        override suspend fun create(source: DecodeSource, options: Options): Decoder? {
            if (isGif(source.source)) return null
            return ImageIODecoder(source.source)
        }
    }
}
