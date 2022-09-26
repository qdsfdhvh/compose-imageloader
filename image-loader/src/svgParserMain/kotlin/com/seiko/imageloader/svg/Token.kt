package com.seiko.imageloader.svg

sealed interface Token

data class TagStart(val name: String) : Token
data class TagEnd(val name: String) : Token
data class Attribute(val name: String, val value: String) : Token
data class Text(val text: String): Token
data class Comment(val text: String): Token
