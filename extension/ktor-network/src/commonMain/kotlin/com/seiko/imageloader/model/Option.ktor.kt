package com.seiko.imageloader.model

import com.seiko.imageloader.option.Options
import com.seiko.imageloader.option.OptionsBuilder

fun OptionsBuilder.ktorRequest(block: KtorRequestDataBuilder.() -> Unit) {
    extra {
        set(KEY_KTOR_REQUEST_DATA, KtorRequestData(block))
    }
}

internal val Options.ktorRequestData: KtorRequestData?
    get() = extra[KEY_KTOR_REQUEST_DATA] as? KtorRequestData

private const val KEY_KTOR_REQUEST_DATA = "KEY_KTOR_REQUEST_DATA"
