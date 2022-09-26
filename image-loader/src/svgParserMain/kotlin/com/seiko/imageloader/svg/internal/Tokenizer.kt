package com.seiko.imageloader.svg.internal

import com.seiko.imageloader.svg.Token

internal class Tokenizer {

    private val tokenBuilders = mutableListOf<TokenBuilder>()

    val tokens: List<Token>
        get() = tokenBuilders.map { it.build() }

    private var currentState: State = DataState

    fun parser(reader: Reader) {
        while (reader.hasNext()) {
            currentState.read(this, reader)
        }
    }

    fun emit(event: TokenEvent) {
        when(event) {
            TagStartOpen -> {
                tokenBuilders.add(TagStartBuilder())
            }
            TagStartClose -> {
                // require(tokenBuilders.last() is TagStartBuilder)
            }
            TagEndOpen -> {
                tokenBuilders.add(TagEndBuilder())
            }
            TagEndClose -> {
                // require(tokenBuilders.last() is TagEndBuilder)
            }
            is TagChar -> {
                val last = tokenBuilders.last() as ContentBuilder
                last.add(event.char)
            }
            CommentOpen -> {

            }
            CommentClose -> {

            }
            is CommentChar -> {

            }
            AttributeOpen -> {
                tokenBuilders.add(AttributeBuilder())
            }
            AttributeClose -> {
                require(tokenBuilders.last() is AttributeBuilder)
            }
            is AttributeChar -> {
                val last = tokenBuilders.last() as AttributeBuilder
                last.addName(event.char)
            }
            is AttributeValueChar -> {
                val last = tokenBuilders.last() as AttributeBuilder
                last.addValue(event.char)
            }
            is Character -> {
                val last = tokenBuilders.lastOrNull()
                if (last is TextBuilder) {
                    last.add(event.char)
                }
                // else {
                //     tokenBuilders.add(
                //         TextBuilder().apply { add(event.char) }
                //     )
                // }
            }
            EOF -> Unit
        }
    }

    fun switch(state: State) {
        currentState = state
    }
}