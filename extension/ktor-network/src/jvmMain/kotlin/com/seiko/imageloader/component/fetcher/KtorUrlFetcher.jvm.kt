package com.seiko.imageloader.component.fetcher

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.jvm.javaio.toInputStream
import okio.BufferedSource
import okio.buffer
import okio.source

internal actual suspend fun ByteReadChannel.source(): BufferedSource {
    return toInputStream().source().buffer()
}

internal actual val httpEngine: HttpClientEngine get() = OkHttp.create()
