package com.seiko.imageloader.component.decoder

import androidx.compose.ui.unit.Density
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.util.SVGPainter
import com.seiko.imageloader.util.isSvg
import okio.BufferedSource
import org.jetbrains.skia.Data
import org.jetbrains.skia.svg.SVGDOM

class SvgDecoder private constructor(
    private val density: Density,
    private val channel: BufferedSource,
) : Decoder {

    override suspend fun decode(): DecodeResult {
        val data = Data.makeFromBytes(channel.readByteArray())
        return DecodeResult.Painter(
            painter = SVGPainter(SVGDOM(data), density),
        )
    }

    class Factory(
        private val density: Density,
    ) : Decoder.Factory {
        override suspend fun create(source: DecodeSource, options: Options): Decoder? {
            if (!isSvg(source.source)) return null
            return SvgDecoder(density, source.source)
        }
    }
}
