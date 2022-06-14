package com.seiko.imageloader.util

expect class WeakReference<T : Any>(referred: T) {
    fun get(): T?
}
