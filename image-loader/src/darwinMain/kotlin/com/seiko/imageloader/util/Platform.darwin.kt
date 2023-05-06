package com.seiko.imageloader.util

import io.ktor.util.toByteArray
import io.ktor.utils.io.ByteReadChannel
import okio.Buffer
import okio.BufferedSource

// TODO
internal actual suspend fun ByteReadChannel.source(): BufferedSource {
    return Buffer().apply { write(toByteArray()) }
}

internal actual suspend fun ByteArray.bufferedSource(): BufferedSource {
    return Buffer().apply { write(this@bufferedSource) }
}
