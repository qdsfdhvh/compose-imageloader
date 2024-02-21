package com.seiko.imageloader.util

import okio.FileSystem

actual typealias WeakReference<T> = java.lang.ref.WeakReference<T>

actual val defaultFileSystem: FileSystem? get() = FileSystem.SYSTEM

actual fun identityHashCode(instance: Any): Int {
    return System.identityHashCode(instance)
}
