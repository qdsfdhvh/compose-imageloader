package com.seiko.imageloader.util

import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.jvm.javaio.copyTo
import io.ktor.utils.io.jvm.javaio.toByteReadChannel
import io.ktor.utils.io.jvm.javaio.toInputStream
import okio.Sink
import okio.Source
import okio.buffer
import okio.source

internal actual inline val Any.identityHashCode: Int
    get() = System.identityHashCode(this)

internal actual fun Source.toByteReadChannel(): ByteReadChannel {
    return buffer().inputStream().toByteReadChannel()
}

internal actual fun ByteReadChannel.toSource(): Source {
    return toInputStream().source().buffer()
}

internal actual suspend fun ByteReadChannel.copyTo(sink: Sink): Long {
    val dst = sink.buffer().outputStream()
    return copyTo(dst, Long.MAX_VALUE)
}
