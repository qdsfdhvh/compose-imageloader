package com.seiko.imageloader.util

import androidx.compose.runtime.InternalComposeApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okio.FileSystem

internal actual val ioDispatcher: CoroutineDispatcher get() = Dispatchers.Default

actual val defaultFileSystem: FileSystem? get() = null

@OptIn(InternalComposeApi::class)
actual fun identityHashCode(instance: Any): Int {
    return androidx.compose.runtime.identityHashCode(instance)
}
