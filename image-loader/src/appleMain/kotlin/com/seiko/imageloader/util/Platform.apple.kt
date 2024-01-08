package com.seiko.imageloader.util

import okio.FileSystem

actual typealias WeakReference<T> = kotlin.native.ref.WeakReference<T>

actual val defaultFileSystem: FileSystem? get() = FileSystem.SYSTEM
