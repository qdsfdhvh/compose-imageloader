package com.seiko.imageloader.svg

import org.junit.Test
import kotlin.test.assertContentEquals

class TokenizerTest {

    @Test
    fun basicXml() {
        val xml = "<html><body><div></div></body></html>"
        val actual = SVG.tokenize(xml)
        assertContentEquals(listOf(
            TagStart("html"),
            TagStart("body"),
            TagStart("div"),
            TagEnd("div"),
            TagEnd("body"),
            TagEnd("html")
        ), actual)
    }

    @Test
    fun basicSVG() {
        val svg = """
            <svg xmlns="http://www.w3.org/2000/svg"
                 width="24px"
                 height="24px"
                 viewBox="0 0 24 24"
                 fill="#000000">
               <path d="M1"></path>
               <path d="M0 0h24v24H0z" fill="none"/>
            </svg>
        """.trimIndent()
        val actual = SVG.tokenize(svg)
        println(actual)
        assertContentEquals(listOf(
            TagStart("svg"),
            Attribute("xmlns", "http://www.w3.org/2000/svg"),
            Attribute("width", "24px"),
            Attribute("height", "24px"),
            Attribute("viewBox", "0 0 24 24"),
            Attribute("fill", "#000000"),

            TagStart("path"),
            Attribute("d", "M1"),
            TagEnd("path"),

            TagStart("path"),
            Attribute("d", "M0 0h24v24H0z"),
            Attribute("fill", "none"),

            TagEnd("svg"),
        ), actual)
    }
}
