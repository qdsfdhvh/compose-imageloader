package com.seiko.imageloader.component.decoder

import com.seiko.imageloader.option.Options
import com.seiko.imageloader.util.GifPainter
import com.seiko.imageloader.util.isGif
import okio.BufferedSource
import okio.use
import org.jetbrains.skia.Codec
import org.jetbrains.skia.Data

class GifDecoder private constructor(
    private val source: BufferedSource,
    private val options: Options,
) : Decoder {
    override suspend fun decode(): DecodeResult {
        val codec = source.use {
            Codec.makeFromData(Data.makeFromBytes(it.readByteArray()))
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
            if (!isGif(source.source)) return null
            return GifDecoder(source.source, options)
        }
    }
}
