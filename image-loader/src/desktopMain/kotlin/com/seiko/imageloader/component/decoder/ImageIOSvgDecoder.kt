package com.seiko.imageloader.component.decoder

import androidx.compose.ui.graphics.toPainter
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.request.SourceResult
import com.seiko.imageloader.util.isSvg
import kotlinx.coroutines.runInterruptible
import okio.BufferedSource
import javax.imageio.ImageIO

class ImageIOSvgDecoder(
    private val channel: BufferedSource,
) : Decoder {

    override suspend fun decode(): DecoderResult {
        val image = runInterruptible {
            ImageIO.read(channel.inputStream())
        }
        return DecodePainterResult(
            painter = image.toPainter(),
        )
    }

    class Factory : Decoder.Factory {
        override suspend fun create(source: SourceResult, options: Options): Decoder? {
            if (!isSvg(source.channel)) return null
            return ImageIOSvgDecoder(source.channel)
        }
    }
}
