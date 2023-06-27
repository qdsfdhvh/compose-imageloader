package com.seiko.imageloader.component.decoder

import com.seiko.imageloader.option.Options
import com.seiko.imageloader.util.GifPainter
import com.seiko.imageloader.util.isGif
import kotlinx.coroutines.CoroutineScope
import okio.BufferedSource
import okio.use
import org.jetbrains.skia.Codec
import org.jetbrains.skia.Data

class GifDecoder private constructor(
    private val source: BufferedSource,
    private val imageScope: CoroutineScope,
    private val options: Options,
) : Decoder {
    override suspend fun decode(): DecodeResult {
        val codec = source.use {
            Codec.makeFromData(Data.makeFromBytes(it.readByteArray()))
        }
        return DecodeResult.Painter(
            painter = GifPainter(
                codec = codec,
                imageScope = imageScope,
                repeatCount = options.repeatCount,
            ),
        )
    }

    class Factory(
        private val imageScope: CoroutineScope,
    ) : Decoder.Factory {
        override suspend fun create(source: DecodeSource, options: Options): Decoder? {
            if (!options.playAnimate) return null
            if (!isGif(source.source)) return null
            return GifDecoder(source.source, imageScope, options)
        }
    }
}
