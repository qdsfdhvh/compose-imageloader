package com.seiko.imageloader.util

import androidx.compose.runtime.AtomicReference
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js

actual class WeakReference<T : Any> actual constructor(referred: T) {
    private val workaroundReference: T = referred // TODO: Properly implement weak reference
    actual fun get(): T? = workaroundReference
    actual fun clear() = Unit
}

actual class AtomicBoolean actual constructor(referred: Boolean) {

    private var atomic = AtomicReference(referred)

    actual fun get(): Boolean = atomic.get()

    actual fun compareAndSet(expect: Boolean, update: Boolean): Boolean {
        return atomic.compareAndSet(expect, update)
    }
}

actual typealias LockObject = Any

internal actual inline fun <R> synchronized(lock: LockObject, block: () -> R): R {
    return block()
}

internal actual val httpEngine: HttpClientEngine get() = Js.create()
