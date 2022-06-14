package com.seiko.imageloader.util

import io.ktor.utils.io.ByteReadChannel
import okio.Sink
import okio.Source

internal actual inline val Any.identityHashCode: Int
    get() = hashCode()

internal actual fun Source.toByteReadChannel(): ByteReadChannel {
    TODO()
}

internal actual fun ByteReadChannel.toSource(): Source {
    TODO()
}

internal actual suspend fun ByteReadChannel.copyTo(sink: Sink): Long {
    TODO()
}
