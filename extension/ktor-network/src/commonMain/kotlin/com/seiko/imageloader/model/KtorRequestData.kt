package com.seiko.imageloader.model

import io.ktor.http.HttpMethod

data class KtorRequestData(
    val method: HttpMethod,
    val headers: Map<String, String>,
)

class KtorRequestDataBuilder internal constructor() {

    var method: HttpMethod = HttpMethod.Get

    private var headers: MutableMap<String, String> = mutableMapOf()

    fun headers(block: MutableMap<String, String>.() -> Unit) {
        headers.apply(block)
    }

    fun build(): KtorRequestData {
        return KtorRequestData(
            method = method,
            headers = headers.toMap(),
        )
    }
}

fun KtorRequestData(block: KtorRequestDataBuilder.() -> Unit): KtorRequestData =
    KtorRequestDataBuilder().apply(block).build()
