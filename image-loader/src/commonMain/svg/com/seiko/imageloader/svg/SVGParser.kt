package com.seiko.imageloader.svg

import com.seiko.imageloader.svg.internal.parseSvg

object SVGParser {
    fun parse(source: String): SVG {
        return parseSvg(source)
    }
}