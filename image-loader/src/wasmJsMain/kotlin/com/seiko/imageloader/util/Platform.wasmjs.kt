package com.seiko.imageloader.util

import androidx.compose.runtime.InternalComposeApi
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

actual val defaultFileSystem: FileSystem? get() = null

@OptIn(InternalComposeApi::class)
actual fun identityHashCode(instance: Any): Int {
    return androidx.compose.runtime.identityHashCode(instance)
}
