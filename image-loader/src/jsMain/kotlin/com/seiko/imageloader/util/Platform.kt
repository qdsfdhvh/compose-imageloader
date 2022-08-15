package com.seiko.imageloader.util

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js
import io.ktor.util.toByteArray
import io.ktor.utils.io.ByteReadChannel
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okio.Buffer
import okio.BufferedSource

actual class WeakReference<T : Any> actual constructor(referred: T) {

    private var weakRef: dynamic

    private var strongRefFallback: T?

    /** The weakly referenced object. If the garbage collector collected the object, this returns null. */
    actual fun get() = if (weakRef == null) strongRefFallback else weakRef.deref() as T?

    init {
        try {
            weakRef = js("new WeakRef(aWrapped)")
            strongRefFallback = null
        } catch (e: Throwable) {
            strongRefFallback = referred
            weakRef = null
        }
    }
}

actual class AtomicBoolean actual constructor(referred: Boolean) {

    private val atomic = atomic(referred)

    actual fun get() = atomic.value

    actual fun compareAndSet(expect: Boolean, update: Boolean): Boolean {
        return atomic.compareAndSet(expect, update)
    }
}

actual typealias LockObject = Any

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

internal actual val httpEngine: HttpClientEngine get() = Js.create()
