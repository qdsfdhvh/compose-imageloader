package com.seiko.imageloader.svg.internal

internal sealed interface TokenEvent

internal object EOF : TokenEvent
internal data class Character(val char: Char) : TokenEvent

internal object TagStartOpen : TokenEvent
internal object TagStartClose : TokenEvent
internal object TagEndOpen : TokenEvent
internal object TagEndClose : TokenEvent
internal data class TagChar(val char: Char) : TokenEvent

internal object AttributeOpen : TokenEvent
internal object AttributeClose : TokenEvent
internal data class AttributeChar(val char: Char) : TokenEvent
internal data class AttributeValueChar(val char: Char) : TokenEvent

internal object CommentOpen : TokenEvent
internal object CommentClose : TokenEvent
internal data class CommentChar(val char: Char) : TokenEvent
