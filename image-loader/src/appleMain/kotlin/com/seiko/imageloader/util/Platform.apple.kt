package com.seiko.imageloader.util

import androidx.compose.runtime.InternalComposeApi
import okio.FileSystem

actual typealias WeakReference<T> = kotlin.native.ref.WeakReference<T>

actual val defaultFileSystem: FileSystem? get() = FileSystem.SYSTEM

@OptIn(InternalComposeApi::class)
actual fun identityHashCode(instance: Any): Int {
    return androidx.compose.runtime.identityHashCode(instance)
}
