package com.seiko.imageloader.util

import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.jvm.javaio.toInputStream
import okio.BufferedSource
import okio.FileSystem
import okio.buffer
import okio.source

actual typealias WeakReference<T> = java.lang.ref.WeakReference<T>

internal actual suspend fun ByteReadChannel.source(): BufferedSource {
    return toInputStream().source().buffer()
}

internal actual val defaultFileSystem: FileSystem? get() = FileSystem.SYSTEM

internal expect fun getMimeTypeFromExtension(extension: String): String?
