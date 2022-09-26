package com.seiko.imageloader.svg.internal

import okio.BufferedSource

internal sealed interface Reader {
    fun next(): Char
    fun hasNext(): Boolean
}

internal class StringReader(source: String) : Reader {

    private val source = source + eof

    private var _position: Int = 0

    override fun next(): Char {
        return source[_position++]
    }

    override fun hasNext(): Boolean {
        return _position < source.length
    }
}

internal class BufferSourceReader(private val source: BufferedSource) : Reader {

    override fun next(): Char {
        return Char(source.readInt())
    }

    override fun hasNext(): Boolean {
        return !source.exhausted()
    }
}
