package com.seiko.imageloader.component.decoder

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.createBitmap
import com.caverock.androidsvg.RenderOptions
import com.caverock.androidsvg.SVG
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.request.SourceResult
import com.seiko.imageloader.size.Scale
import com.seiko.imageloader.size.isOriginal
import com.seiko.imageloader.size.toPx
import com.seiko.imageloader.util.DecodeUtils
import com.seiko.imageloader.util.toBitmapConfig
import com.seiko.imageloader.util.toSoftware
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.coroutines.runInterruptible
import okio.buffer
import okio.source
import kotlin.math.roundToInt

/**
 * A [Decoder] that uses [AndroidSVG](https://bigbadaboom.github.io/androidsvg/) to decode SVG
 * files.
 *
 * @param useViewBoundsAsIntrinsicSize If true, uses the SVG's view bounds as the intrinsic size for
 *  the SVG. If false, uses the SVG's width/height as the intrinsic size for the SVG.
 */
class SvgDecoder @JvmOverloads constructor(
    private val context: Context,
    private val channel: ByteReadChannel,
    private val options: Options,
    private val useViewBoundsAsIntrinsicSize: Boolean = true
) : Decoder {

    override suspend fun decode() = runInterruptible {
        val svg = SVG.getFromInputStream(channel.toInputStream())

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
        val (dstWidth, dstHeight) = getDstSize(svgWidth, svgHeight, options.scale)
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
            image = bitmap.asImageBitmap(),
            // isSampled = true // SVGs can always be re-decoded at a higher resolution.
        )
    }

    private fun getDstSize(srcWidth: Float, srcHeight: Float, scale: Scale): Pair<Float, Float> {
        return if (options.size.isOriginal) {
            val dstWidth = if (srcWidth > 0) srcWidth else DEFAULT_SIZE
            val dstHeight = if (srcHeight > 0) srcHeight else DEFAULT_SIZE
            dstWidth to dstHeight
        } else {
            val (dstWidth, dstHeight) = options.size
            dstWidth.toPx(scale).toFloat() to dstHeight.toPx(scale).toFloat()
        }
    }

    class Factory constructor(
        private val context: Context,
        val useViewBoundsAsIntrinsicSize: Boolean = true
    ) : Decoder.Factory {

        override fun create(source: SourceResult, options: Options): Decoder? {
            if (!isApplicable(source)) return null
            return SvgDecoder(context, source.channel, options, useViewBoundsAsIntrinsicSize)
        }

        private fun isApplicable(source: SourceResult): Boolean {
            return source.mimeType == MIME_TYPE_SVG
                || DecodeUtils.isSvg(source.channel.toInputStream().source().buffer())
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
        private const val DEFAULT_SIZE = 512f
        // const val CSS_KEY = "coil#css"
    }
}
