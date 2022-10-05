package com.seiko.imageloader.svg

import com.seiko.imageloader.svg.internal.SVGParser

class SVG {

    // val width: String = ""
    // val height: String = ""
    // val viewBox: String = ""
    // val fill: String = ""

    internal fun reset() {
    }

    companion object {
        fun parse(source: String): SVG {
            return SVGParser().run {
                parser(source)
                svg
            }
        }
    }
}