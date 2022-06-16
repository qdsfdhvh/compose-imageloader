package com.seiko.imageloader.util

import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.bits.withMemory
import okio.ByteString
import okio.ByteString.Companion.toByteString

internal actual suspend fun ByteReadChannel.rangeEquals(offset: Long, bytes: ByteString): Boolean {
    return withMemory(bytes.size) {
        peekTo(it, 0, offset)
        it.buffer.toByteString() == bytes
    }
}

internal actual suspend fun ByteReadChannel.request(byteCount: Int): Boolean {
    return withMemory(byteCount) {
        peekTo(it, 0, 0) == byteCount.toLong()
    }
}

internal actual suspend fun ByteReadChannel.buffer(index: Int): Byte {
    return withMemory(1) {
        peekTo(it, 0, index.toLong())
        it.buffer.get()
    }
}
