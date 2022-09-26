package com.seiko.imageloader.svg.internal

internal sealed interface State {
    fun read(tokenizer: Tokenizer, reader: Reader)
}

internal object DataState : State {
    override fun read(tokenizer: Tokenizer, reader: Reader) {
        when (val char = reader.next()) {
            '<' -> {
                tokenizer.switch(TagStartOpenState)
            }
            eof -> {
                tokenizer.emit(EOF)
            }
            else -> {
                tokenizer.emit(Character(char))
            }
        }
    }
}

internal object TagStartOpenState : State {
    override fun read(tokenizer: Tokenizer, reader: Reader) {
        when (val char = reader.next()) {
            in asciiAlpha -> {
                tokenizer.emit(TagStartOpen)
                tokenizer.emit(TagChar(char))
                tokenizer.switch(TagStartOpenNameState)
            }
            '>' -> {
                tokenizer.emit(TagStartClose)
                tokenizer.switch(DataState)
            }
            '?' -> {
                tokenizer.emit(CommentOpen)
                tokenizer.switch(BogusCommentState)
            }
            '/' -> {
                tokenizer.emit(TagEndOpen)
                tokenizer.switch(TagEndOpenState)
            }
            '!' -> {
                // do no thing
            }
            in asciiIgnore -> {
                tokenizer.switch(BeforeAttributeNameState)
            }
            // eof -> {
            //     tokenizer.emit(EOF)
            // }
        }
    }
}

internal object TagStartOpenNameState : State {
    override fun read(tokenizer: Tokenizer, reader: Reader) {
        when (val char = reader.next()) {
            in asciiAlpha -> {
                tokenizer.emit(TagChar(char))
            }
            '>' -> {
                tokenizer.emit(TagStartClose)
                tokenizer.switch(DataState)
            }
            in asciiIgnore -> {
                tokenizer.switch(BeforeAttributeNameState)
            }
        }
    }
}

internal object BeforeAttributeNameState : State {
    override fun read(tokenizer: Tokenizer, reader: Reader) {
        when (val char = reader.next()) {
            in asciiIgnore -> {
                // Ignore the character.
            }
            '=' -> {
                tokenizer.switch(BeforeAttributeValueState)
            }
            else -> {
                tokenizer.emit(AttributeOpen)
                tokenizer.emit(AttributeChar(char))
                tokenizer.switch(AttributeNameState)
            }
        }
    }
}

internal object AttributeNameState : State {
    override fun read(tokenizer: Tokenizer, reader: Reader) {
        when (val char = reader.next()) {
            '=' -> {
                tokenizer.switch(BeforeAttributeValueState)
            }
            NULL -> {
                tokenizer.emit(AttributeChar('\uFFFD'))
            }
            else -> {
                tokenizer.emit(AttributeChar(char))
            }
        }
    }
}

internal object BeforeAttributeValueState : State {
    override fun read(tokenizer: Tokenizer, reader: Reader) {
        when (val char = reader.next()) {
            in asciiIgnore -> {
                // Ignore the character.
            }
            '"' -> {
                tokenizer.switch(AttributeValueDoubleQuotedState)
            }
            '\'' -> {
                tokenizer.switch(AttributeValueSingleQuotedState)
            }
            '>' -> {
                tokenizer.emit(TagStartClose)
                tokenizer.switch(DataState)
            }
            else -> {

            }
        }
    }
}

internal object AttributeValueDoubleQuotedState : State {
    override fun read(tokenizer: Tokenizer, reader: Reader) {
        when (val char = reader.next()) {
            '"' -> {
                tokenizer.switch(AfterAttributeValueQuotedState)
            }
            '\u0000' -> {
                tokenizer.emit(AttributeValueChar('\uFFFD'))
            }
            else -> {
                tokenizer.emit(AttributeValueChar(char))
            }
        }
    }
}

internal object AttributeValueSingleQuotedState : State {
    override fun read(tokenizer: Tokenizer, reader: Reader) {
        when (val char = reader.next()) {
            '\'' -> {
                tokenizer.switch(AfterAttributeValueQuotedState)
            }
            '\u0000' -> {
                tokenizer.emit(AttributeValueChar('\uFFFD'))
            }
            else -> {
                tokenizer.emit(AttributeValueChar(char))
            }
        }
    }
}

internal object AfterAttributeValueQuotedState : State {
    override fun read(tokenizer: Tokenizer, reader: Reader) {
        when (val char = reader.next()) {
            in asciiIgnore -> {
                tokenizer.switch(BeforeAttributeNameState)
            }
            '>' -> {
                tokenizer.emit(TagStartClose)
                tokenizer.switch(DataState)
            }
        }
    }
}

internal object TagEndOpenState : State {
    override fun read(tokenizer: Tokenizer, reader: Reader) {
        when (val char = reader.next()) {
            in asciiAlpha -> {
                tokenizer.emit(TagChar(char))
            }
            '>' -> {
                tokenizer.emit(TagEndClose)
                tokenizer.switch(DataState)
            }
            // eof -> {
            //     tokenizer.emit(EOF)
            // }
        }
    }
}

internal object BogusCommentState : State {
    override fun read(tokenizer: Tokenizer, reader: Reader) {
        when (val char = reader.next()) {
            '>' -> {
                tokenizer.emit(CommentClose)
                tokenizer.switch(DataState)
            }
            NULL -> {
                tokenizer.emit(CommentChar('\uFFFD'))
            }
            else -> {
                tokenizer.emit(CommentChar(char))
            }
        }
    }
}


private val asciiUppercase = 'A'..'Z'
private val asciiLowercase = 'a'..'z'
private val asciiAlpha = asciiUppercase + asciiLowercase
private val asciiDigit = '0'..'9'
private val asciiAlphanumeric = asciiAlpha + asciiDigit

// private val asciiUpperHexDigit = 'A'..'F'
// private val asciiLowerHexDigit = 'a'..'f'
// private val asciiHexDigit = asciiUpperHexDigit + asciiLowerHexDigit
// private val c0control = 0x00..0x001F
// private val control = c0control + (0x007F..0x009F)
private val asciiIgnore = charArrayOf('\u0009', '\u000A', '\u000C', '\u0020')
private const val NULL = '\u0000'
