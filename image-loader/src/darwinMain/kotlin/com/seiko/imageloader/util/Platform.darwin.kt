package com.seiko.imageloader.util

import io.ktor.util.toByteArray
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okio.Buffer
import okio.BufferedSource

// TODO
internal actual suspend fun ByteReadChannel.source(): BufferedSource {
    return Buffer().apply { write(toByteArray()) }
}

internal actual suspend fun ByteArray.bufferedSource(): BufferedSource {
    return Buffer().apply { write(this@bufferedSource) }
}

internal actual val ioDispatcher: CoroutineDispatcher get() = Dispatchers.Default
