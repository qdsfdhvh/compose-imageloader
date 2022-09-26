package com.seiko.imageloader.svg

import com.seiko.imageloader.svg.internal.StringReader
import com.seiko.imageloader.svg.internal.Tokenizer

class SVG {
    companion object {
        internal fun tokenize(source: String): List<Token> {
            return Tokenizer().run {
                parser(StringReader(source))
                tokens
            }
        }
    }
}
