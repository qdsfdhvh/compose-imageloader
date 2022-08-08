package com.seiko.imageloader.util

import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.jvm.javaio.toInputStream
import okio.BufferedSource
import okio.buffer
import okio.source

actual typealias WeakReference<T> = java.lang.ref.WeakReference<T>

actual typealias AtomicBoolean = java.util.concurrent.atomic.AtomicBoolean

actual typealias LockObject = Any

actual inline fun <R> synchronized(lock: LockObject, block: () -> R): R {
    return kotlin.synchronized(lock, block)
}

internal actual suspend fun ByteReadChannel.source(): BufferedSource {
    return toInputStream().source().buffer()
}
