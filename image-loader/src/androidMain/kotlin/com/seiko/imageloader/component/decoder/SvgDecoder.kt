package com.seiko.imageloader.component.decoder

import android.graphics.Canvas
import android.graphics.RectF
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isUnspecified
import androidx.core.graphics.createBitmap
import com.caverock.androidsvg.RenderOptions
import com.caverock.androidsvg.SVG
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.model.SourceResult
import com.seiko.imageloader.util.DecodeUtils
import com.seiko.imageloader.util.isSvg
import com.seiko.imageloader.util.toBitmapConfig
import com.seiko.imageloader.util.toSoftware
import kotlinx.coroutines.runInterruptible
import kotlin.math.roundToInt

/**
 * A [Decoder] that uses [AndroidSVG](https://bigbadaboom.github.io/androidsvg/) to decode SVG
 * files.
 *
 * @param useViewBoundsAsIntrinsicSize If true, uses the SVG's view bounds as the intrinsic size for
 *  the SVG. If false, uses the SVG's width/height as the intrinsic size for the SVG.
 */
class SvgDecoder constructor(
    private val source: SourceResult,
    private val options: Options,
    private val useViewBoundsAsIntrinsicSize: Boolean,
) : Decoder {

    override suspend fun decode(): DecodeResult {
        val size = options.sizeResolver.size()
        return runInterruptible {
            val svg = SVG.getFromInputStream(source.channel.inputStream())

            if (svg.documentSVGVersion != null && svg.documentSVGVersion.startsWith("2")) {
                throw RuntimeException("Un support SVG version '2.0'")
            }

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
            val (dstWidth, dstHeight) = getDstSize(svgWidth, svgHeight, size)
            if (svgWidth > 0 && svgHeight > 0) {
                val multiplier = DecodeUtils.computeSizeMultiplier(
                    srcWidth = svgWidth,
                    srcHeight = svgHeight,
                    dstWidth = dstWidth,
                    dstHeight = dstHeight,
                    scale = options.scale
                )
                bitmapWidth = (multiplier * svgWidth).toInt()
                bitmapHeight = (multiplier * svgHeight).toInt()
            } else {
                bitmapWidth = dstWidth.roundToInt()
                bitmapHeight = dstHeight.roundToInt()
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
            )
        }
    }

    private fun getDstSize(srcWidth: Float, srcHeight: Float, size: Size): Pair<Float, Float> {
        return if (size.isUnspecified || size.isEmpty()) {
            val dstWidth = if (srcWidth > 0) srcWidth else DEFAULT_SIZE
            val dstHeight = if (srcHeight > 0) srcHeight else DEFAULT_SIZE
            dstWidth to dstHeight
        } else {
            val (dstWidth, dstHeight) = size
            dstWidth to dstHeight
        }
    }

    class Factory constructor(
        private val useViewBoundsAsIntrinsicSize: Boolean = true
    ) : Decoder.Factory {

        override suspend fun create(source: SourceResult, options: Options): Decoder? {
            if (!isApplicable(source)) return null
            return SvgDecoder(source, options, useViewBoundsAsIntrinsicSize)
        }

        private fun isApplicable(source: SourceResult): Boolean {
            return source.mimeType == MIME_TYPE_SVG || isSvg(source.channel)
        }
    }

    companion object {
        private const val MIME_TYPE_SVG = "image/svg+xml"
        private const val DEFAULT_SIZE = 512f
    }
}
