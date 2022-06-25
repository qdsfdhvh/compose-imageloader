package com.seiko.imageloader.util

import kotlinx.atomicfu.locks.SynchronizedObject
import kotlin.native.ref.WeakReference

actual typealias WeakReference<T> = WeakReference<T>

actual typealias LockObject = SynchronizedObject

actual inline fun <R> synchronized(lock: LockObject, block: () -> R): R {
    return kotlinx.atomicfu.locks.synchronized(lock, block)
}
