package com.seiko.imageloader.util

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okio.BufferedSource
import okio.FileSystem
import okio.buffer
import okio.source

actual typealias WeakReference<T> = java.lang.ref.WeakReference<T>

actual typealias LockObject = Any

internal actual inline fun <R> synchronized(lock: LockObject, block: () -> R): R {
    return kotlin.synchronized(lock, block)
}

internal actual suspend fun ByteReadChannel.source(): BufferedSource {
    return toInputStream().source().buffer()
}

internal actual val ioDispatcher: CoroutineDispatcher get() = Dispatchers.IO

internal actual val httpEngine: HttpClientEngine get() = OkHttp.create()

internal actual val defaultFileSystem: FileSystem? get() = FileSystem.SYSTEM

internal expect fun getMimeTypeFromExtension(extension: String): String?
