package com.seiko.imageloader.component.fetcher

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js
import io.ktor.util.toByteArray
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.cancel
import okio.Buffer
import okio.BufferedSource

internal actual suspend fun ByteReadChannel.source(): BufferedSource {
    return Buffer().apply {
        write(toByteArray())
        this@source.cancel()
    }
}

internal actual val httpEngine: HttpClientEngine
    get() = Js.create()
