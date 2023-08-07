package com.seiko.imageloader.util

import androidx.compose.runtime.AtomicReference
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js

actual class WeakReference<T : Any> actual constructor(referred: T) {

    private var weakRef: dynamic

    private var strongRefFallback: T?

    /** The weakly referenced object. If the garbage collector collected the object, this returns null. */
    actual fun get(): T? = if (weakRef == null) strongRefFallback else weakRef.deref() as T?

    actual fun clear() = if (weakRef == null) strongRefFallback = null else weakRef = null

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
