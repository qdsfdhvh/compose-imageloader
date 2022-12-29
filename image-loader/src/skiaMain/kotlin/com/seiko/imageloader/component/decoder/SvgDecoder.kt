package com.seiko.imageloader.component.decoder

import androidx.compose.ui.unit.Density
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.request.SourceResult
import com.seiko.imageloader.util.SVGPainter
import com.seiko.imageloader.util.isSvg
import okio.BufferedSource
import org.jetbrains.skia.Data
import org.jetbrains.skia.svg.SVGDOM

class SvgDecoder(
    private val density: Density,
    private val channel: BufferedSource,
) : Decoder {

    override suspend fun decode(): DecodeResult {
        val data = Data.makeFromBytes(channel.readByteArray())
        return DecodePainterResult(
            painter = SVGPainter(SVGDOM(data), density),
        )
    }

    class Factory(
        private val density: Density,
    ) : Decoder.Factory {
        override suspend fun create(source: SourceResult, options: Options): Decoder? {
            if (!isSvg(source.channel)) return null
            return SvgDecoder(density, source.channel)
        }
    }
}
