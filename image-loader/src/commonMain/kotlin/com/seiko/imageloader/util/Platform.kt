package com.seiko.imageloader.util

import kotlinx.coroutines.CoroutineDispatcher
import okio.FileSystem

expect class WeakReference<T : Any>(referred: T) {
    fun get(): T?

    fun clear()
}

internal expect val ioDispatcher: CoroutineDispatcher

expect val defaultFileSystem: FileSystem?
