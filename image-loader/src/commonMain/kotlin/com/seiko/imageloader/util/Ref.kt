package com.seiko.imageloader.util

expect class WeakReference<T>(value: T) {
    fun get(): T?
}