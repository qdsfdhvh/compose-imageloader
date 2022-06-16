package com.seiko.imageloader.component.decoder

import com.seiko.imageloader.request.Options
import com.seiko.imageloader.request.SourceResult
import com.seiko.imageloader.util.GifPainter
import com.seiko.imageloader.util.isGif
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.coroutines.runInterruptible
import org.jetbrains.skia.Codec
import org.jetbrains.skia.Data

class GifDecoder(
    private val channel: ByteReadChannel,
) : Decoder {
    override suspend fun decode(): DecoderResult = runInterruptible {
        val codec = Codec.makeFromData(Data.makeFromBytes(channel.toInputStream().readBytes()))
        DecodePainterResult(
            painter = GifPainter(codec),
        )
    }

    class Factory : Decoder.Factory {
        override suspend fun create(source: SourceResult, options: Options): Decoder? {
            if (!isGif(source.channel)) return null
            return GifDecoder(source.channel)
        }
    }
}
