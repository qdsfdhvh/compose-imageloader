package com.seiko.imageloader.component.decoder

import com.seiko.imageloader.model.ImageSource
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.util.GifPainter
import com.seiko.imageloader.util.isGif
import org.jetbrains.skia.Codec
import org.jetbrains.skia.Data

class GifDecoder private constructor(
    private val source: ImageSource,
    private val options: Options,
) : Decoder {
    override suspend fun decode(): DecodeResult {
        val codec = source.use {
            Codec.makeFromData(Data.makeFromBytes(it.bufferedSource.readByteArray()))
        }
        return DecodeResult.OfPainter(
            painter = GifPainter(
                codec = codec,
                repeatCount = options.repeatCount,
            ),
        )
    }

    class Factory : Decoder.Factory {
        override fun create(source: DecodeSource, options: Options): Decoder? {
            if (!options.playAnimate) return null
            if (options.isBitmap) return null
            if (!isGif(source.imageSource.bufferedSource)) return null
            return GifDecoder(source.imageSource, options)
        }
    }
}
