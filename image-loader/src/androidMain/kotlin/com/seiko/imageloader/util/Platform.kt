package com.seiko.imageloader.util

actual typealias WeakReference<T> = java.lang.ref.WeakReference<T>

actual typealias LockObject = Any

actual inline fun <R> synchronized(lock: LockObject, block: () -> R): R {
    return kotlin.synchronized(lock, block)
}
