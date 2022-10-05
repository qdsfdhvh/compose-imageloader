package com.seiko.imageloader.svg

import com.seiko.imageloader.svg.internal.parseLength
import nl.adaptivity.xmlutil.EventType
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import nl.adaptivity.xmlutil.attributes
import nl.adaptivity.xmlutil.core.KtXmlReader
import nl.adaptivity.xmlutil.core.impl.multiplatform.StringReader
import nl.adaptivity.xmlutil.core.impl.multiplatform.use
import nl.adaptivity.xmlutil.toCName
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalXmlUtilApi
class TokenizerTest {

    @Test
    fun testParseLength() {
        assertEquals(parseLength("24px"), Length(24f, Unit.px))
        assertEquals(parseLength("24.2em"), Length(24.2f, Unit.em))
        assertEquals(parseLength("100ex"), Length(100f, Unit.ex))
        assertEquals(parseLength("16in"), Length(16f, Unit.`in`))
        assertEquals(parseLength("24cm"), Length(24f, Unit.cm))
        assertEquals(parseLength("24mm"), Length(24f, Unit.mm))
        assertEquals(parseLength("24pt"), Length(24f, Unit.pt))
        assertEquals(parseLength("24pc"), Length(24f, Unit.pc))
        assertEquals(parseLength("1%"), Length(1f, Unit.percent))
    }

    @Test
    fun basicSVG() {
        val svg = """
            <svg xmlns="http://www.w3.org/2000/svg"
                 width="24px"
                 height="24px"
                 viewBox="0 0 24 24"
                 fill="#000000">
               <path d="M0 0h24v24H0z" fill="none"/>
            </svg>
        """.trimIndent()
        KtXmlReader(StringReader(svg)).use { reader ->
            while(reader.hasNext()) {
                when(val type = reader.next()) {
                    EventType.START_DOCUMENT -> {
                        println("START_DOCUMENT")
                    }
                    EventType.START_ELEMENT -> {
                        val cName = reader.name.toCName()
                        val tag = reader.localName.ifEmpty { cName }
                        println("START_ELEMENT tag=$tag, localName=${reader.localName}, cname=$cName")
                        reader.attributes.forEach { attribute ->
                            println("${attribute.localName}=${attribute.value}")
                        }
                    }
                    EventType.END_ELEMENT -> {
                        val cName = reader.name.toCName()
                        val tag = reader.localName.ifEmpty { cName }
                        println("END_ELEMENT tag=$tag, localName=${reader.localName}, cname=$cName")
                    }
                    EventType.END_DOCUMENT -> {
                        println("END_DOCUMENT")
                    }
                    else -> Unit
                }
            }
        }
        // val actual: SVG = defaultXmlFormat.decodeFromString(svg)
        // assertEquals(
        //     SVG(
        //         width = "24px",
        //         height = "24px",
        //         viewBox = "0 0 24 24",
        //         fill = "#000000",
        //         // paths = listOf(
        //         //     SVG.Path(d = "M1", fill = ""),
        //         //     SVG.Path(d = "M0 0h24v24H0z", fill = ""),
        //         // )
        //         path = SVG.Path(d = "M0 0h24v24H0z", fill = "none"),
        //     ),
        //     actual,
        // )
    }
}
