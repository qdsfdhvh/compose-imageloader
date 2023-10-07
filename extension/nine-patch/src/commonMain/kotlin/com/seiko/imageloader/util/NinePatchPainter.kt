package com.seiko.imageloader.util

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import com.seiko.imageloader.model.NinePatchCenterRect
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

internal class NinePatchPainter(
    private val image: ImageBitmap,
    private val centerSlice: NinePatchCenterRect,
    private val scale: Float = 1f,
    private val filterQuality: FilterQuality = FilterQuality.Medium,
) : Painter() {

    private val centerWidth = max(1, centerSlice.width)
    private val centerHeight = max(1, centerSlice.height)
    private val widthLeft = centerSlice.left
    private val widthRight = image.width - centerSlice.right
    private val heightTop = centerSlice.top
    private val heightBottom = image.height - centerSlice.bottom

    // Source Offset
    private val offsetTopLeft = IntOffset.Zero
    private val offsetTop = IntOffset(widthLeft, 0)
    private val offsetTopRight = IntOffset(centerSlice.right, 0)
    private val offsetLeft = IntOffset(0, heightTop)
    private val offsetCenter = IntOffset(centerSlice.left, heightTop)
    private val offsetRight = IntOffset(centerSlice.right, heightTop)
    private val offsetBottomLeft = IntOffset(0, centerSlice.bottom)
    private val offsetBottom = IntOffset(centerSlice.left, centerSlice.bottom)
    private val offsetBottomRight = IntOffset(centerSlice.right, centerSlice.bottom)

    // Source Size
    private val sizeTopLeft = IntSize(widthLeft, heightTop)
    private val sizeTop = IntSize(centerWidth, heightTop)
    private val sizeTopRight = IntSize(widthRight, heightTop)
    private val sizeLeft = IntSize(widthLeft, centerHeight)
    private val sizeCenter = IntSize(centerWidth, centerHeight)
    private val sizeRight = IntSize(widthRight, centerHeight)
    private val sizeBottomLeft = IntSize(widthLeft, heightBottom)
    private val sizeBottom = IntSize(centerWidth, heightBottom)
    private val sizeBottomRight = IntSize(widthRight, heightBottom)

    private val size: IntSize = IntSize(image.width, image.height) // validateSize(IntOffset.Zero, surfaceSize)
    private var alpha: Float = 1.0f
    private var colorFilter: ColorFilter? = null

    override fun DrawScope.onDraw() {
        if (image.width < centerSlice.right || image.height < centerSlice.bottom) {
            println("incorrect bitmap")
            return
        }
        val drawWidth = this@onDraw.size.width.roundToInt()
        val drawHeight = this@onDraw.size.height.roundToInt()
        val factor = min(drawWidth / image.width.toFloat(), drawHeight / image.height.toFloat())
        val drawScale = min(1f, factor * scale)
        val scaleLeft = max(1, (widthLeft * drawScale).toInt())
        val scaleRight = max(1, (widthRight * drawScale).toInt())
        val scaleTop = max(1, (heightTop * drawScale).toInt())
        val scaleBottom = max(1, (heightBottom * drawScale).toInt())
        val scaleWidth = max(1, drawWidth - scaleLeft - scaleRight)
        val scaleHeight = max(1, drawHeight - scaleTop - scaleBottom)

        // Center
        drawImage(
            image,
            offsetCenter,
            sizeCenter,
            dstOffset = IntOffset(scaleLeft, scaleTop),
            dstSize = IntSize(scaleWidth, scaleHeight),
            alpha = alpha,
            colorFilter = colorFilter,
            filterQuality = filterQuality,
        )
        // Top
        drawImage(
            image,
            offsetTop,
            sizeTop,
            dstOffset = IntOffset(scaleLeft, 0),
            dstSize = IntSize(scaleWidth, scaleTop),
            alpha = alpha,
            colorFilter = colorFilter,
            filterQuality = filterQuality,
        )
        // Left
        drawImage(
            image,
            offsetLeft,
            sizeLeft,
            dstOffset = IntOffset(0, scaleTop),
            dstSize = IntSize(scaleLeft, scaleHeight),
            alpha = alpha,
            colorFilter = colorFilter,
            filterQuality = filterQuality,
        )
        // Right
        drawImage(
            image,
            offsetRight,
            sizeRight,
            dstOffset = IntOffset(drawWidth - scaleRight, scaleTop),
            dstSize = IntSize(scaleRight, scaleHeight),
            alpha = alpha,
            colorFilter = colorFilter,
            filterQuality = filterQuality,
        )
        // Bottom
        drawImage(
            image,
            offsetBottom,
            sizeBottom,
            dstOffset = IntOffset(scaleLeft, drawHeight - scaleBottom),
            dstSize = IntSize(scaleWidth, scaleBottom),
            alpha = alpha,
            colorFilter = colorFilter,
            filterQuality = filterQuality,
        )
        // TopLeft
        drawImage(
            image,
            offsetTopLeft,
            sizeTopLeft,
            dstOffset = IntOffset.Zero,
            dstSize = IntSize(scaleLeft, scaleTop),
            alpha = alpha,
            colorFilter = colorFilter,
            filterQuality = filterQuality,
        )
        // TopRight
        drawImage(
            image,
            offsetTopRight,
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
            offsetBottomLeft,
            sizeBottomLeft,
            dstOffset = IntOffset(0, drawHeight - scaleBottom),
            dstSize = IntSize(scaleLeft, scaleBottom),
            alpha = alpha,
            colorFilter = colorFilter,
            filterQuality = filterQuality,
        )
        // BottomRight
        drawImage(
            image,
            offsetBottomRight,
            sizeBottomRight,
            dstOffset = IntOffset(drawWidth - scaleRight, drawHeight - scaleBottom),
            dstSize = IntSize(scaleRight, scaleBottom),
            alpha = alpha,
            colorFilter = colorFilter,
            filterQuality = filterQuality,
        )
    }

    override val intrinsicSize: Size get() = size.toSize()

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
        if (centerSlice != other.centerSlice) return false
        if (scale != other.scale) return false
        if (filterQuality != other.filterQuality) return false
        return true
    }

    override fun hashCode(): Int {
        var result = image.hashCode()
        result = 31 * result + centerSlice.hashCode()
        result = 31 * result + size.hashCode()
        result = 31 * result + scale.hashCode()
        result = 31 * result + filterQuality.hashCode()
        return result
    }

    override fun toString(): String {
        return "NinePathPainter(image=$image, centerSlice=$centerSlice, scale=$scale)"
    }
}
