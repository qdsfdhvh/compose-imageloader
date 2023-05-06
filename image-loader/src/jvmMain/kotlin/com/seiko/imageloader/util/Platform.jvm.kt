package com.seiko.imageloader.util

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.jvm.javaio.toInputStream
import okio.BufferedSource
import okio.buffer
import okio.source
import java.io.ByteArrayInputStream

actual typealias WeakReference<T> = java.lang.ref.WeakReference<T>

actual typealias AtomicBoolean = java.util.concurrent.atomic.AtomicBoolean

actual typealias LockObject = Any

internal actual inline fun <R> synchronized(lock: LockObject, block: () -> R): R {
    return kotlin.synchronized(lock, block)
}

internal actual suspend fun ByteReadChannel.source(): BufferedSource {
    return toInputStream().source().buffer()
}

internal actual suspend fun ByteArray.bufferedSource() = ByteArrayInputStream(this).source().buffer()

internal actual val httpEngine: HttpClientEngine get() = OkHttp.create()

internal expect fun getMimeTypeFromExtension(extension: String): String?
