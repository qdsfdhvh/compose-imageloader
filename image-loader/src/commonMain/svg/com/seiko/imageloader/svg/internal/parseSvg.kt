package com.seiko.imageloader.svg.internal

import com.seiko.imageloader.svg.SVG
import nl.adaptivity.xmlutil.EventType
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import nl.adaptivity.xmlutil.QName
import nl.adaptivity.xmlutil.XmlEvent
import nl.adaptivity.xmlutil.attributes
import nl.adaptivity.xmlutil.core.KtXmlReader
import nl.adaptivity.xmlutil.core.impl.multiplatform.StringReader
import nl.adaptivity.xmlutil.core.impl.multiplatform.use
import nl.adaptivity.xmlutil.toCName

internal fun parseSvg(source: String): SVG {
    return SVGBuilder()
        .apply { parser(source) }
        .build()
}

@OptIn(ExperimentalXmlUtilApi::class)
private fun SVGBuilder.parser(source: String) {
    KtXmlReader(StringReader(source)).use { reader ->
        while (reader.hasNext()) {
            when (reader.next()) {
                EventType.START_DOCUMENT -> {
                    startDocument()
                }

                EventType.START_ELEMENT -> {
                    startElement(
                        namespaceURI = reader.namespaceURI,
                        localName = reader.localName,
                        qName = reader.name,
                        attributes = reader.attributes,
                    )
                }

                EventType.END_ELEMENT -> {
                    endElement(
                        namespaceURI = reader.namespaceURI,
                        localName = reader.localName,
                        qName = reader.name,
                    )
                }

                EventType.TEXT, EventType.ENTITY_REF, EventType.CDSECT -> {
                    text(reader.text)
                }

                EventType.PROCESSING_INSTRUCTION -> {
                    handleProcessingInstruction(TextScanner(reader.text))
                }

                EventType.COMMENT -> Unit
                EventType.DOCDECL -> Unit
                EventType.IGNORABLE_WHITESPACE -> Unit
                EventType.ATTRIBUTE -> Unit

                EventType.END_DOCUMENT -> {
                    endDocument()
                }
            }
        }
    }
}

private fun SVGBuilder.startDocument() = Unit

private fun SVGBuilder.endDocument() = Unit

private fun SVGBuilder.startElement(
    namespaceURI: String,
    localName: String,
    qName: QName,
    attributes: Array<out XmlEvent.Attribute>,
) {
    if (namespaceURI.isNotEmpty() && namespaceURI != SVG_NAMESPACE) {
        return
    }
    val tag = localName.ifEmpty { qName.toCName() }
    parseStartElement(tag, attributes)
}

private fun SVGBuilder.endElement(
    namespaceURI: String,
    localName: String,
    qName: QName,
) {

}

private fun SVGBuilder.text(characters: String) {

}

private fun SVGBuilder.handleProcessingInstruction(
    scanner: TextScanner,
) {
    val instruction = scanner.nextToken()
    val attributes = parseProcessingInstructionAttributes(scanner)

}

private fun parseProcessingInstructionAttributes(
    scanner: TextScanner,
): Map<String, String> {
    return emptyMap()
}

private const val SVG_NAMESPACE = "http://www.w3.org/2000/svg"