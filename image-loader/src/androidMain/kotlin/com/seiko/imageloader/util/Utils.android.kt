package com.seiko.imageloader.util

import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.bits.withMemory
import okio.ByteString
import okio.ByteString.Companion.toByteString

internal actual inline val Any.identityHashCode: Int
    get() = System.identityHashCode(this)

internal suspend fun ByteReadChannel.rangeEquals(offset: Long, bytes: ByteString): Boolean {
    return withMemory(bytes.size) {
        peekTo(it, 0, offset)
        it.buffer.toByteString() == bytes
    }
}
