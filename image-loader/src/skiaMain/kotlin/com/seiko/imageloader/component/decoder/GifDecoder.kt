package com.seiko.imageloader.component.decoder

import com.seiko.imageloader.option.Options
import com.seiko.imageloader.model.SourceResult
import com.seiko.imageloader.util.GifPainter
import com.seiko.imageloader.util.isGif
import kotlinx.coroutines.CoroutineScope
import okio.BufferedSource
import org.jetbrains.skia.Codec
import org.jetbrains.skia.Data

class GifDecoder(
    private val channel: BufferedSource,
    private val imageScope: CoroutineScope,
) : Decoder {
    override suspend fun decode(): DecodeResult {
        val codec = Codec.makeFromData(Data.makeFromBytes(channel.readByteArray()))
        return DecodePainterResult(
            painter = GifPainter(codec, imageScope),
        )
    }

    class Factory(
        private val imageScope: CoroutineScope,
    ) : Decoder.Factory {
        override suspend fun create(source: SourceResult, options: Options): Decoder? {
            if (!isGif(source.channel)) return null
            return GifDecoder(source.channel, imageScope)
        }
    }
}
