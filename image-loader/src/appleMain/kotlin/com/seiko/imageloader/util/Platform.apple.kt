package com.seiko.imageloader.util

import kotlinx.atomicfu.locks.SynchronizedObject
import okio.FileSystem
import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
actual typealias WeakReference<T> = kotlin.native.ref.WeakReference<T>

actual typealias LockObject = SynchronizedObject

internal actual inline fun <R> synchronized(lock: LockObject, block: () -> R): R {
    return kotlinx.atomicfu.locks.synchronized(lock, block)
}

internal actual val defaultFileSystem: FileSystem? get() = FileSystem.SYSTEM
