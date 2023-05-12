package com.seiko.imageloader.component.decoder

import com.seiko.imageloader.option.Options
import com.seiko.imageloader.util.GifPainter
import com.seiko.imageloader.util.isGif
import kotlinx.coroutines.CoroutineScope
import okio.BufferedSource
import org.jetbrains.skia.Codec
import org.jetbrains.skia.Data

class GifDecoder private constructor(
    private val channel: BufferedSource,
    private val imageScope: CoroutineScope,
    private val options: Options,
) : Decoder {
    override suspend fun decode(): DecodeResult {
        val codec = Codec.makeFromData(Data.makeFromBytes(channel.readByteArray()))
        return DecodeResult.Painter(
            painter = GifPainter(
                codec = codec,
                imageScope = imageScope,
                playAnimate = options.playAnimate,
                repeatCount = options.repeatCount,
            ),
        )
    }

    class Factory(
        private val imageScope: CoroutineScope,
    ) : Decoder.Factory {
        override suspend fun create(source: DecodeSource, options: Options): Decoder? {
            if (!isGif(source.source)) return null
            return GifDecoder(source.source, imageScope, options)
        }
    }
}
