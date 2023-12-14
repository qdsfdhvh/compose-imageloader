package com.seiko.imageloader.util

import okio.FileSystem

actual typealias WeakReference<T> = java.lang.ref.WeakReference<T>

internal actual val defaultFileSystem: FileSystem? get() = FileSystem.SYSTEM

internal expect fun getMimeTypeFromExtension(extension: String): String?
