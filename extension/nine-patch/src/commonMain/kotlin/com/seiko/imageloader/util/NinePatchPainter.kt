package com.seiko.imageloader.util

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.seiko.imageloader.model.NinePatchData
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

internal class NinePatchPainter(
    private val image: ImageBitmap,
    private val ninePatchData: NinePatchData,
) : Painter() {

    private val scale get() = ninePatchData.scale
    private val skipPadding get() = ninePatchData.skipPadding
    private val maxFactor get() = ninePatchData.maxFactor
    private val filterQuality get() = ninePatchData.filterQuality

    // must have value for center size
    private val centerWidth = max(1, ninePatchData.right - ninePatchData.left)
    private val centerHeight = max(1, ninePatchData.bottom - ninePatchData.top)

    private val widthLeft = ninePatchData.left
    private val widthRight = image.width - ninePatchData.right
    private val heightTop = ninePatchData.top
    private val heightBottom = image.height - ninePatchData.bottom

    // Source Offset
    private val offsetTopLeft = IntOffset(skipPadding, skipPadding)
    private val offsetTop = IntOffset(widthLeft, skipPadding)
    private val offsetTopRight = IntOffset(ninePatchData.right, skipPadding)
    private val offsetLeft = IntOffset(skipPadding, heightTop)
    private val offsetCenter = IntOffset(ninePatchData.left, heightTop)
    private val offsetRight = IntOffset(ninePatchData.right, heightTop)
    private val offsetBottomLeft = IntOffset(skipPadding, ninePatchData.bottom)
    private val offsetBottom = IntOffset(ninePatchData.left, ninePatchData.bottom)
    private val offsetBottomRight = IntOffset(ninePatchData.right, ninePatchData.bottom)

    // Source Size
    private val sizeTopLeft = IntSize(widthLeft, heightTop)
    private val sizeTop = IntSize(centerWidth, heightTop)
    private val sizeTopRight = IntSize(widthRight - skipPadding, heightTop)
    private val sizeLeft = IntSize(widthLeft, centerHeight)
    private val sizeCenter = IntSize(centerWidth, centerHeight)
    private val sizeRight = IntSize(widthRight - skipPadding, centerHeight)
    private val sizeBottomLeft = IntSize(widthLeft, heightBottom - skipPadding)
    private val sizeBottom = IntSize(centerWidth, heightBottom - skipPadding)
    private val sizeBottomRight = IntSize(widthRight - skipPadding, heightBottom - skipPadding)

    private var alpha: Float = 1.0f
    private var colorFilter: ColorFilter? = null

    override fun DrawScope.onDraw() {
        if (image.width < ninePatchData.right || image.height < ninePatchData.bottom) {
            println("incorrect bitmap")
            return
        }

        val drawWidth = this@onDraw.size.width.roundToInt()
        val drawHeight = this@onDraw.size.height.roundToInt()

        val factor = min(drawWidth / image.width.toFloat(), drawHeight / image.height.toFloat())
        val drawScale = min(maxFactor, factor * scale)

        val scaleLeft = (widthLeft * drawScale).roundToInt()
        val scaleRight = (widthRight * drawScale).roundToInt()
        val scaleTop = (heightTop * drawScale).roundToInt()
        val scaleBottom = (heightBottom * drawScale).roundToInt()

        val scaleWidth = max(1, drawWidth - scaleLeft - scaleRight)
        val scaleHeight = max(1, drawHeight - scaleTop - scaleBottom)

        // Center
        drawImage(
            image,
            srcOffset = offsetCenter,
            srcSize = sizeCenter,
            dstOffset = IntOffset(scaleLeft, scaleTop),
            dstSize = IntSize(scaleWidth, scaleHeight),
            alpha = alpha,
            colorFilter = colorFilter,
            filterQuality = filterQuality,
        )
        // Top
        drawImage(
            image,
            srcOffset = offsetTop,
            srcSize = sizeTop,
            dstOffset = IntOffset(scaleLeft, 0),
            dstSize = IntSize(scaleWidth, scaleTop),
            alpha = alpha,
            colorFilter = colorFilter,
            filterQuality = filterQuality,
        )
        // Left
        drawImage(
            image,
            srcOffset = offsetLeft,
            srcSize = sizeLeft,
            dstOffset = IntOffset(0, scaleTop),
            dstSize = IntSize(scaleLeft, scaleHeight),
            alpha = alpha,
            colorFilter = colorFilter,
            filterQuality = filterQuality,
        )
        // Right
        drawImage(
            image,
            srcOffset = offsetRight,
            srcSize = sizeRight,
            dstOffset = IntOffset(drawWidth - scaleRight, scaleTop),
            dstSize = IntSize(scaleRight, scaleHeight),
            alpha = alpha,
            colorFilter = colorFilter,
            filterQuality = filterQuality,
        )
        // Bottom
        drawImage(
            image,
            srcOffset = offsetBottom,
            srcSize = sizeBottom,
            dstOffset = IntOffset(scaleLeft, drawHeight - scaleBottom),
            dstSize = IntSize(scaleWidth, scaleBottom),
            alpha = alpha,
            colorFilter = colorFilter,
            filterQuality = filterQuality,
        )
        // TopLeft
        drawImage(
            image,
            srcOffset = offsetTopLeft,
            srcSize = sizeTopLeft,
            dstOffset = IntOffset.Zero,
            dstSize = IntSize(scaleLeft, scaleTop),
            alpha = alpha,
            colorFilter = colorFilter,
            filterQuality = filterQuality,
        )
        // TopRight
        drawImage(
            image,
            srcOffset = offsetTopRight,
            sizeTopRight,
            dstOffset = IntOffset(drawWidth - scaleRight, 0),
            dstSize = IntSize(scaleRight, scaleTop),
            alpha = alpha,
            colorFilter = colorFilter,
            filterQuality = filterQuality,
        )
        // BottomLeft
        drawImage(
            image,
            srcOffset = offsetBottomLeft,
            srcSize = sizeBottomLeft,
            dstOffset = IntOffset(0, drawHeight - scaleBottom),
            dstSize = IntSize(scaleLeft, scaleBottom),
            alpha = alpha,
            colorFilter = colorFilter,
            filterQuality = filterQuality,
        )
        // BottomRight
        drawImage(
            image,
            srcOffset = offsetBottomRight,
            srcSize = sizeBottomRight,
            dstOffset = IntOffset(drawWidth - scaleRight, drawHeight - scaleBottom),
            dstSize = IntSize(scaleRight, scaleBottom),
            alpha = alpha,
            colorFilter = colorFilter,
            filterQuality = filterQuality,
        )
    }

    override val intrinsicSize: Size
        get() = Size.Unspecified

    override fun applyAlpha(alpha: Float): Boolean {
        this.alpha = alpha
        return true
    }

    override fun applyColorFilter(colorFilter: ColorFilter?): Boolean {
        this.colorFilter = colorFilter
        return true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is NinePatchPainter) return false
        if (image != other.image) return false
        if (ninePatchData != other.ninePatchData) return false
        return true
    }

    override fun hashCode(): Int {
        var result = image.hashCode()
        result = 31 * result + ninePatchData.hashCode()
        return result
    }

    override fun toString(): String {
        return "NinePathPainter(image=$image, ninePatchData=$ninePatchData, scale=$scale)"
    }
}
