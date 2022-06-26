package com.seiko.imageloader.util

import io.ktor.utils.io.ByteReadChannel
import okio.BufferedSource

expect class WeakReference<T : Any>(referred: T) {
    fun get(): T?
}

expect class LockObject()

expect inline fun <R> synchronized(lock: LockObject, block: () -> R): R

internal expect suspend fun ByteReadChannel.source(): BufferedSource
