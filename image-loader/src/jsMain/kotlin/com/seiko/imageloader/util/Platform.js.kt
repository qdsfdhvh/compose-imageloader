package com.seiko.imageloader.util

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okio.FileSystem

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

internal actual val ioDispatcher: CoroutineDispatcher get() = Dispatchers.Default

internal actual val httpEngine: HttpClientEngine get() = Js.create()

internal actual val defaultFileSystem: FileSystem? get() = null
