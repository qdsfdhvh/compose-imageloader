package com.seiko.imageloader.component.decoder

import androidx.compose.ui.unit.Density
import com.caverock.androidsvg.SVG
import com.seiko.imageloader.model.mimeType
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.util.SVGPainter
import com.seiko.imageloader.util.isSvg

/**
 * A [Decoder] that uses [AndroidSVG](https://bigbadaboom.github.io/androidsvg/) to decode SVG
 * files.
 */
class SvgDecoder private constructor(
    private val source: DecodeSource,
    private val density: Density,
    private val options: Options,
) : Decoder {

    override suspend fun decode(): DecodeResult {
        val svg = SVG.getFromInputStream(source.source.inputStream())
        val requestSize = options.sizeResolver.run {
            density.size()
        }
        return DecodeResult.Painter(
            painter = SVGPainter(svg, density, requestSize),
        )
    }

    class Factory constructor(
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
