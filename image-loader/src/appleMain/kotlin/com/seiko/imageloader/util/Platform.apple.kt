package com.seiko.imageloader.util

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import okio.FileSystem
import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
actual typealias WeakReference<T> = kotlin.native.ref.WeakReference<T>

actual typealias LockObject = SynchronizedObject

internal actual inline fun <R> synchronized(lock: LockObject, block: () -> R): R {
    return kotlinx.atomicfu.locks.synchronized(lock, block)
}

internal actual val ioDispatcher: CoroutineDispatcher get() = Dispatchers.IO

internal actual val httpEngine: HttpClientEngine get() = Darwin.create()

internal actual val defaultFileSystem: FileSystem? get() = FileSystem.SYSTEM
