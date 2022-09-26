package com.seiko.imageloader.svg.internal

import com.seiko.imageloader.svg.Attribute
import com.seiko.imageloader.svg.Comment
import com.seiko.imageloader.svg.TagEnd
import com.seiko.imageloader.svg.TagStart
import com.seiko.imageloader.svg.Text
import com.seiko.imageloader.svg.Token

internal sealed interface TokenBuilder {
    fun build(): Token
}

internal abstract class ContentBuilder : TokenBuilder {
    protected val sb = StringBuilder()
    fun add(char: Char) = sb.append(char)
}

internal class TagStartBuilder : ContentBuilder() {
    override fun build(): Token = TagStart(sb.toString())
}

internal class TagEndBuilder : ContentBuilder() {
    override fun build(): Token = TagEnd(sb.toString())
}

internal class TextBuilder : ContentBuilder() {
    override fun build(): Token = Text(sb.toString())
}

internal class CommentBuilder : ContentBuilder() {
    override fun build(): Token = Comment(sb.toString())
}

internal class AttributeBuilder : TokenBuilder {
    private val name = StringBuilder()
    private val value = StringBuilder()
    fun addName(char: Char) = name.append(char)
    fun addValue(char: Char) = value.append(char)
    override fun build(): Token = Attribute(
        name = name.toString(),
        value = value.toString(),
    )
}
