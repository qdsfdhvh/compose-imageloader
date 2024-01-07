package com.seiko.imageloader.component.decoder

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Density
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.util.SVGPainter
import okio.BufferedSource
import okio.use
import org.jetbrains.skia.Data
import org.jetbrains.skia.svg.SVGDOM
import org.jetbrains.skia.svg.SVGLength
import org.jetbrains.skia.svg.SVGLengthUnit
import org.jetbrains.skia.svg.SVGPreserveAspectRatio
import org.jetbrains.skia.svg.SVGPreserveAspectRatioAlign
import kotlin.jvm.JvmInline

@JvmInline
private value class SkiaSvgDom(
    val dom: SVGDOM,
) : SvgDom {
    override val width: Float
        get() = dom.root?.width?.withUnit(SVGLengthUnit.PX)?.value ?: 0f

    override val height: Float
        get() = dom.root?.height?.withUnit(SVGLengthUnit.PX)?.value ?: 0f

    override fun draw(canvas: Canvas, size: Size) {
        dom.root?.width = SVGLength(size.width, SVGLengthUnit.PX)
        dom.root?.height = SVGLength(size.height, SVGLengthUnit.PX)
        dom.root?.preserveAspectRatio = SVGPreserveAspectRatio(SVGPreserveAspectRatioAlign.NONE)
        dom.render(canvas.nativeCanvas)
    }
}

internal actual fun createSVGPainter(source: BufferedSource, density: Density, options: Options): SVGPainter {
    val dom = source.use {
        SVGDOM(Data.makeFromBytes(it.readByteArray()))
    }
    return SVGPainter(
        dom = SkiaSvgDom(dom),
        density = density,
        requestSize = options.size,
    )
}

internal actual fun defaultDensity(options: Options): Density {
    return Density(2f)
}
