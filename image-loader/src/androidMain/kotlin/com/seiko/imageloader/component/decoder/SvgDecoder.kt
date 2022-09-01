package com.seiko.imageloader.component.decoder

import android.graphics.Canvas
import android.graphics.RectF
import androidx.core.graphics.createBitmap
import com.caverock.androidsvg.RenderOptions
import com.caverock.androidsvg.SVG
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.request.SourceResult
import com.seiko.imageloader.util.DecodeUtils
import com.seiko.imageloader.util.isSvg
import com.seiko.imageloader.util.toBitmapConfig
import com.seiko.imageloader.util.toSoftware
import kotlinx.coroutines.runInterruptible

/**
 * A [Decoder] that uses [AndroidSVG](https://bigbadaboom.github.io/androidsvg/) to decode SVG
 * files.
 *
 * @param useViewBoundsAsIntrinsicSize If true, uses the SVG's view bounds as the intrinsic size for
 *  the SVG. If false, uses the SVG's width/height as the intrinsic size for the SVG.
 */
class SvgDecoder @JvmOverloads constructor(
    private val source: SourceResult,
    private val options: Options,
    private val useViewBoundsAsIntrinsicSize: Boolean = true
) : Decoder {

    override suspend fun decode() = runInterruptible {
        val svg = SVG.getFromInputStream(source.channel.inputStream())

        val svgWidth: Float
        val svgHeight: Float
        val viewBox: RectF? = svg.documentViewBox
        if (useViewBoundsAsIntrinsicSize && viewBox != null) {
            svgWidth = viewBox.width()
            svgHeight = viewBox.height()
        } else {
            svgWidth = svg.documentWidth
            svgHeight = svg.documentHeight
        }

        val bitmapWidth: Int
        val bitmapHeight: Int
        if (svgWidth > 0 && svgHeight > 0) {
            val multiplier = DecodeUtils.computeSizeMultiplier(
                srcWidth = svgWidth,
                srcHeight = svgHeight,
                dstWidth = svgWidth,
                dstHeight = svgHeight,
                scale = options.scale
            )
            bitmapWidth = (multiplier * svgWidth).toInt()
            bitmapHeight = (multiplier * svgHeight).toInt()
        } else {
            bitmapWidth = DEFAULT_SIZE
            bitmapHeight = DEFAULT_SIZE
        }

        // Set the SVG's view box to enable scaling if it is not set.
        if (viewBox == null && svgWidth > 0 && svgHeight > 0) {
            svg.setDocumentViewBox(0f, 0f, svgWidth, svgHeight)
        }

        svg.setDocumentWidth("100%")
        svg.setDocumentHeight("100%")

        val bitmap = createBitmap(bitmapWidth, bitmapHeight, options.config.toBitmapConfig().toSoftware())
        // val renderOptions = options.parameters.css()?.let { RenderOptions().css(it) }
        val renderOptions = RenderOptions()
        svg.renderToCanvas(Canvas(bitmap), renderOptions)

        DecodeImageResult(
            image = bitmap,
            // isSampled = true // SVGs can always be re-decoded at a higher resolution.
        )
    }

    class Factory constructor(
        val useViewBoundsAsIntrinsicSize: Boolean = true
    ) : Decoder.Factory {

        override suspend fun create(source: SourceResult, options: Options): Decoder? {
            if (!isApplicable(source)) return null
            return SvgDecoder(source, options, useViewBoundsAsIntrinsicSize)
        }

        private suspend fun isApplicable(source: SourceResult): Boolean {
            return source.mimeType == MIME_TYPE_SVG || isSvg(source.channel)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            return other is Factory &&
                useViewBoundsAsIntrinsicSize == other.useViewBoundsAsIntrinsicSize
        }

        override fun hashCode() = useViewBoundsAsIntrinsicSize.hashCode()
    }

    companion object {
        private const val MIME_TYPE_SVG = "image/svg+xml"
        private const val DEFAULT_SIZE = 512
        // const val CSS_KEY = "coil#css"
    }
}
