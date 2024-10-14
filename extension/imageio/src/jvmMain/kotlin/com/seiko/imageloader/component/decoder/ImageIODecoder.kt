package com.seiko.imageloader.component.decoder

import androidx.compose.ui.graphics.toPainter
import com.seiko.imageloader.model.ImageSource
import com.seiko.imageloader.model.InputStreamImageSource
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.util.isGif
import kotlinx.coroutines.runInterruptible
import javax.imageio.ImageIO

class ImageIODecoder(
    private val source: ImageSource,
) : Decoder {

    override suspend fun decode(): DecodeResult {
        val image = runInterruptible {
            ImageIO.read(
                if (source is InputStreamImageSource) {
                    source.inputStream
                } else {
                    source.bufferedSource.inputStream()
                },
            )
        }
        return DecodeResult.OfPainter(
            painter = image.toPainter(),
        )
    }

    class Factory : Decoder.Factory {
        override fun create(source: DecodeSource, options: Options): Decoder? {
            if (isGif(source.imageSource.bufferedSource)) return null
            return ImageIODecoder(source.imageSource)
        }
    }
}
