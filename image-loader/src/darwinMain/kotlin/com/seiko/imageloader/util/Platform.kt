package com.seiko.imageloader.util

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import io.ktor.util.toByteArray
import io.ktor.utils.io.ByteReadChannel
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okio.Buffer
import okio.BufferedSource

actual typealias WeakReference<T> = kotlin.native.ref.WeakReference<T>

actual class AtomicBoolean actual constructor(referred: Boolean) {

    private val atomic = atomic(referred)

    actual fun get() = atomic.value

    actual fun compareAndSet(expect: Boolean, update: Boolean): Boolean {
        return atomic.compareAndSet(expect, update)
    }
}

actual typealias LockObject = SynchronizedObject

internal actual inline fun <R> synchronized(lock: LockObject, block: () -> R): R {
    return kotlinx.atomicfu.locks.synchronized(lock, block)
}

// TODO
internal actual suspend fun ByteReadChannel.source(): BufferedSource {
    return Buffer().apply { write(toByteArray()) }
}

internal actual suspend fun ByteArray.bufferedSource(): BufferedSource {
    return Buffer().apply { write(this@bufferedSource) }
}

internal actual val ioDispatcher: CoroutineDispatcher get() = Dispatchers.Default

internal actual val httpEngine: HttpClientEngine get() = Darwin.create()
