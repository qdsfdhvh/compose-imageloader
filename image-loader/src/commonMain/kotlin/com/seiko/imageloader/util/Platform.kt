package com.seiko.imageloader.util

expect class WeakReference<T : Any>(referred: T) {
    fun get(): T?
}

expect class LockObject()

expect inline fun <R> synchronized(lock: LockObject, block: () -> R): R
