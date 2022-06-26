package com.seiko.imageloader.component.decoder

import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.unit.Density
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.request.SourceResult
import com.seiko.imageloader.util.isSvg
import io.github.aakira.napier.Napier
import okio.BufferedSource

class SvgDecoder(
    private val density: Density,
    private val channel: BufferedSource,
) : Decoder {

    override suspend fun decode(): DecoderResult {
        return DecodePainterResult(
            painter = loadSvgPainter(channel.inputStream(), density),
        )
    }

    class Factory(
        private val density: Density,
    ) : Decoder.Factory {
        override suspend fun create(source: SourceResult, options: Options): Decoder? {
            // Napier.d { "check ${source.request.data} is svg?" }
            if (!isSvg(source.channel.peek())) {
                Napier.d { "${source.request.data} not svg" }
                val str = source.channel.peek().readByteString(50)
                Napier.d { "bad source: ${str.toString()}" }
                return null
            }
            // Napier.d { "${source.request.data} is svg" }
            return SvgDecoder(density, source.channel)
        }
    }
}
