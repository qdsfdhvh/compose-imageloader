package com.seiko.imageloader.util

import io.ktor.util.toByteArray
import io.ktor.utils.io.ByteReadChannel
import kotlinx.atomicfu.locks.SynchronizedObject
import okio.Buffer
import okio.BufferedSource
import kotlin.native.ref.WeakReference

actual typealias WeakReference<T> = WeakReference<T>

actual typealias LockObject = SynchronizedObject

actual inline fun <R> synchronized(lock: LockObject, block: () -> R): R {
    return kotlinx.atomicfu.locks.synchronized(lock, block)
}

// TODO
internal actual suspend fun ByteReadChannel.source(): BufferedSource {
    return Buffer().apply { write(toByteArray()) }
}
