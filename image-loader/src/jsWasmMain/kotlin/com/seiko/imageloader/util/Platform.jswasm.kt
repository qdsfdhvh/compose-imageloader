package com.seiko.imageloader.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okio.FileSystem

internal actual val ioDispatcher: CoroutineDispatcher get() = Dispatchers.Default

internal actual val defaultFileSystem: FileSystem? get() = null
