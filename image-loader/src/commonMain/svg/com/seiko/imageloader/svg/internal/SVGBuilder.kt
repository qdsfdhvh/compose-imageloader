package com.seiko.imageloader.svg.internal

import com.seiko.imageloader.svg.Length
import com.seiko.imageloader.svg.SVG

internal class SVGBuilder {
    var x: Length = Length.Zero
    var y: Length = Length.Zero

    var width: Length = Length.Zero
    var height: Length = Length.Zero

    var version: String = ""

    fun build(): SVG {
        return SVG(
            x = x,
            y = y,
            width = width,
            height = height,
            version = version,
        )
    }
}
