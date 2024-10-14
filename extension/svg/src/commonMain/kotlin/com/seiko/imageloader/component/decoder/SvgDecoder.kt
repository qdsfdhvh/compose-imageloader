package com.seiko.imageloader.component.decoder

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.unit.Density
import com.seiko.imageloader.model.ImageSource
import com.seiko.imageloader.model.mimeType
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.util.SVGPainter
import com.seiko.imageloader.util.isSvg
import okio.BufferedSource

class SvgDecoder private constructor(
    private val source: ImageSource,
    private val density: Density,
    private val options: Options,
) : Decoder {

    override suspend fun decode(): DecodeResult {
        return DecodeResult.OfPainter(
            painter = createSVGPainter(source.bufferedSource, density, options),
        )
    }

    class Factory(
        private val density: Density? = null,
    ) : Decoder.Factory {

        override fun create(source: DecodeSource, options: Options): Decoder? {
            if (!isApplicable(source)) return null
            return SvgDecoder(
                source = source.imageSource,
                density = density ?: defaultDensity(options),
                options = options,
            )
        }

        private fun isApplicable(source: DecodeSource): Boolean {
            return source.extra.mimeType == MIME_TYPE_SVG || isSvg(source.imageSource.bufferedSource)
        }
    }

    companion object {
        private const val MIME_TYPE_SVG = "image/svg+xml"
    }
}

interface SvgDom {
    val width: Float
    val height: Float
    fun draw(canvas: Canvas, size: Size)
}

internal expect fun createSVGPainter(source: BufferedSource, density: Density, options: Options): SVGPainter

internal expect fun defaultDensity(options: Options): Density
