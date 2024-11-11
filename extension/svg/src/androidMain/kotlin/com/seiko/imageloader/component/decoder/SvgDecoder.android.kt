package com.seiko.imageloader.component.decoder

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Density
import com.caverock.androidsvg.PreserveAspectRatio
import com.caverock.androidsvg.SVG
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.option.androidContext
import com.seiko.imageloader.util.SVGPainter
import okio.BufferedSource

/**
 * A [Decoder] that uses [AndroidSVG](https://bigbadaboom.github.io/androidsvg/) to decode SVG
 * files.
 */
@JvmInline
private value class AndroidSvgDom(
    val dom: SVG,
) : SvgDom {
    override val width: Float
        get() = dom.documentWidth

    override val height: Float
        get() = dom.documentHeight

    override fun draw(canvas: Canvas, size: Size) {
        if (dom.documentViewBox == null) {
            dom.setDocumentViewBox(0f, 0f, dom.documentWidth, dom.documentHeight)
        }
        dom.documentWidth = size.width
        dom.documentHeight = size.height
        dom.documentPreserveAspectRatio = PreserveAspectRatio.STRETCH
        dom.renderToCanvas(canvas.nativeCanvas)
    }
}

internal actual fun createSVGPainter(source: BufferedSource, density: Density, options: Options): SVGPainter {
    val dom = source.use {
        SVG.getFromInputStream(it.inputStream())
    }
    return SVGPainter(
        dom = AndroidSvgDom(dom),
        density = density,
        requestSize = options.size,
        imageBitmapConfig = options.imageBitmapConfig,
    )
}

internal actual fun defaultDensity(options: Options): Density {
    return Density(options.androidContext)
}
