package com.seiko.imageloader.util

import androidx.compose.runtime.InternalComposeApi
import kotlinx.coroutines.CoroutineDispatcher
import okio.FileSystem

expect class WeakReference<T : Any>(referred: T) {
    fun get(): T?

    fun clear()
}

internal expect val ioDispatcher: CoroutineDispatcher

expect val defaultFileSystem: FileSystem?

@OptIn(InternalComposeApi::class)
internal fun identityHashCode(instance: Any): Int {
    return androidx.compose.runtime.identityHashCode(instance)
}
