package com.seiko.imageloader.util

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import com.caverock.androidsvg.PreserveAspectRatio
import com.caverock.androidsvg.SVG
import kotlin.math.ceil

internal class SVGPainter(
    private val dom: SVG,
    private val density: Density,
    private val requestSize: Size = Size.Unspecified,
) : Painter() {

    private val defaultSizePx: Size = run {
        if (requestSize.isSpecified) {
            return@run requestSize
        }
        val width = dom.documentWidth
        val height = dom.documentHeight
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
    private val drawCache = DrawCache()

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
            drawCache.drawCachedImage(
                IntSize(ceil(size.width).toInt(), ceil(size.height).toInt()),
                density = this,
                layoutDirection,
            ) {
                drawSvg(size)
            }
        }

        drawCache.drawInto(this, alpha, colorFilter)
    }

    private fun DrawScope.drawSvg(size: Size) {
        drawIntoCanvas { canvas ->
            dom.documentWidth = size.width
            dom.documentHeight = size.height
            dom.documentPreserveAspectRatio = PreserveAspectRatio.STRETCH
            dom.renderToCanvas(canvas.nativeCanvas)
        }
    }
}
