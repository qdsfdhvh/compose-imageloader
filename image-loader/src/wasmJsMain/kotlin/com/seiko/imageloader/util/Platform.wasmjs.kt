package com.seiko.imageloader.util

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okio.FileSystem

actual class WeakReference<T : Any> actual constructor(referred: T) {

    private var strongRefFallback: T? = referred

    /** The weakly referenced object. If the garbage collector collected the object, this returns null. */
    actual fun get(): T? = strongRefFallback

    actual fun clear() {
        strongRefFallback = null
    }
}

internal actual val ioDispatcher: CoroutineDispatcher get() = Dispatchers.Default

internal actual val httpEngine: HttpClientEngine get() = Js.create()

internal actual val defaultFileSystem: FileSystem? get() = null
