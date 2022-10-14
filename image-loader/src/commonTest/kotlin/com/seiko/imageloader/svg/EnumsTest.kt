package com.seiko.imageloader.svg

import com.seiko.imageloader.svg.internal.SvgAttr
import com.seiko.imageloader.svg.internal.SvgElement
import kotlin.test.Test
import kotlin.test.assertEquals

class EnumsTest {

    @Test
    fun test() {
        assertEquals(SvgElement.parse("a"), SvgElement.a)
        assertEquals(SvgElement.parse("view"), SvgElement.view)
    }

    @Test
    fun testSvgAttr() {
        assertEquals(SvgAttr.parse("class"), SvgAttr.`class`)
        assertEquals(SvgAttr.parse("aria-atomic"), SvgAttr.aria_atomic)
    }
}