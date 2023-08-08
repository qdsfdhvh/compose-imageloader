package com.seiko.imageloader.util

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js

actual class WeakReference<T : Any> actual constructor(referred: T) {
    private val workaroundReference: T = referred // TODO: Properly implement weak reference
    actual fun get(): T? = workaroundReference
    actual fun clear() = Unit
}

actual typealias LockObject = Any

internal actual inline fun <R> synchronized(lock: LockObject, block: () -> R): R {
    return block()
}

internal actual val httpEngine: HttpClientEngine get() = Js.create()
