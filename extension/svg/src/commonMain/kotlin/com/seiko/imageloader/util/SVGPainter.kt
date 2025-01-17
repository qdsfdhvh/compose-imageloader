package com.seiko.imageloader.util

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import com.seiko.imageloader.component.decoder.SvgDom
import kotlin.math.ceil

@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
internal class SVGPainter(
    private val dom: SvgDom,
    private val density: Density,
    private val requestSize: Size = Size.Unspecified,
    private val imageBitmapConfig: ImageBitmapConfig = ImageBitmapConfig.Argb8888,
) : Painter() {

    private val defaultSizePx: Size = run {
        if (requestSize.isSpecified) {
            return@run requestSize
        }
        val width = dom.width
        val height = dom.height
        if (width == 0f && height == 0f) {
            Size.Unspecified
        } else {
            Size(width, height)
        }
    }

    override val intrinsicSize: Size
        get() {
            return if (defaultSizePx.isSpecified) {
                defaultSizePx * density.density
            } else {
                Size.Unspecified
            }
        }

    private var previousDrawSize: Size = Size.Unspecified
    private var alpha: Float = 1.0f
    private var colorFilter: ColorFilter? = null

    // with caching into bitmap FPS is 3x-4x higher (tested with idea-logo.svg with 30x30 icons)
    private val drawCache = androidx.compose.ui.graphics.vector.DrawCache()

    override fun applyAlpha(alpha: Float): Boolean {
        this.alpha = alpha
        return true
    }

    override fun applyColorFilter(colorFilter: ColorFilter?): Boolean {
        this.colorFilter = colorFilter
        return true
    }

    override fun DrawScope.onDraw() {
        if (previousDrawSize != size) {
            previousDrawSize = size

            drawCache.drawCachedImage(
                config = imageBitmapConfig,
                size = IntSize(ceil(size.width).toInt(), ceil(size.height).toInt()),
                density = this,
                layoutDirection = layoutDirection,
            ) {
                drawSvg(size)
            }
        }

        drawCache.drawInto(this, alpha, colorFilter)
    }

    private fun DrawScope.drawSvg(size: Size) {
        drawIntoCanvas { canvas ->
            dom.draw(canvas, size)
        }
    }

    override fun hashCode(): Int {
        var result = dom.hashCode()
        result = 31 * result + density.hashCode()
        result = 31 * result + requestSize.hashCode()
        result = 31 * result + imageBitmapConfig.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SVGPainter) return false

        if (dom != other.dom) return false
        if (density != other.density) return false
        if (requestSize != other.requestSize) return false
        if (imageBitmapConfig != other.imageBitmapConfig) return false
        return true
    }
}
