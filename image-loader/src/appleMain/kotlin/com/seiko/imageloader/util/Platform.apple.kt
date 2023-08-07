package com.seiko.imageloader.util

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import okio.FileSystem

actual typealias WeakReference<T> = kotlin.native.ref.WeakReference<T>

actual class AtomicBoolean actual constructor(referred: Boolean) {

    private val atomic = atomic(referred)

    actual fun get() = atomic.value

    actual fun compareAndSet(expect: Boolean, update: Boolean): Boolean {
        return atomic.compareAndSet(expect, update)
    }
}

actual typealias LockObject = SynchronizedObject

internal actual inline fun <R> synchronized(lock: LockObject, block: () -> R): R {
    return kotlinx.atomicfu.locks.synchronized(lock, block)
}

internal actual val ioDispatcher: CoroutineDispatcher get() = Dispatchers.IO

internal actual val httpEngine: HttpClientEngine get() = Darwin.create()

internal actual val defaultFileSystem: FileSystem? get() = FileSystem.SYSTEM
