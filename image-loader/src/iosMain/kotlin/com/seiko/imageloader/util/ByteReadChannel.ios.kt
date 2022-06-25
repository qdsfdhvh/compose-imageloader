package com.seiko.imageloader.util

import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.bits.withMemory
import kotlinx.cinterop.readBytes
import okio.ByteString

internal actual suspend fun ByteReadChannel.rangeEquals(offset: Long, bytes: ByteString): Boolean {
    return withMemory(bytes.size) {
        peekTo(it, 0, offset, bytes.size.toLong(), bytes.size.toLong())
        it.pointer.readBytes(bytes.size).contentEquals(bytes.toByteArray())
    }
}

internal actual suspend fun ByteReadChannel.request(byteCount: Int): Boolean {
    return withMemory(byteCount) {
        peekTo(it, 0, 0, byteCount.toLong(), byteCount.toLong()) == byteCount.toLong()
    }
}

internal actual suspend fun ByteReadChannel.buffer(index: Int): Byte {
    return withMemory(1) {
        peekTo(it, 0, index.toLong(), 1, 1)
        it.pointer.readBytes(1)[0]
    }
}
