@file:OptIn(ExperimentalXmlUtilApi::class)

package com.seiko.imageloader.svg.internal

import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import nl.adaptivity.xmlutil.core.KtXmlReader

// internal sealed interface SVGState {
//     fun read(tokenizer: SVGParser, reader: KtXmlReader)
// }

internal object SVGElementState {
    fun read(tokenizer: SVGParser, reader: KtXmlReader) {

    }
}