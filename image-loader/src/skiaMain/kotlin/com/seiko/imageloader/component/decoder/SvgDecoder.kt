package com.seiko.imageloader.component.decoder

import androidx.compose.ui.unit.Density
import com.seiko.imageloader.model.mimeType
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.util.SVGPainter
import com.seiko.imageloader.util.isSvg
import org.jetbrains.skia.Data
import org.jetbrains.skia.svg.SVGDOM

class SvgDecoder private constructor(
    private val source: DecodeSource,
    private val density: Density,
    private val options: Options,
) : Decoder {

    override suspend fun decode(): DecodeResult {
        val data = Data.makeFromBytes(source.source.readByteArray())
        val requestSize = options.sizeResolver.run {
            density.size()
        }
        return DecodeResult.Painter(
            painter = SVGPainter(SVGDOM(data), density, requestSize),
        )
    }

    class Factory(
        private val density: Density,
    ) : Decoder.Factory {
        override suspend fun create(source: DecodeSource, options: Options): Decoder? {
            if (!isApplicable(source)) return null
            return SvgDecoder(
                source = source,
                density = density,
                options = options,
            )
        }

        private fun isApplicable(source: DecodeSource): Boolean {
            return source.extra.mimeType == MIME_TYPE_SVG || isSvg(source.source)
        }
    }

    companion object {
        private const val MIME_TYPE_SVG = "image/svg+xml"
    }
}
