package com.seiko.imageloader.util

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okio.BufferedSource
import okio.FileSystem

expect class WeakReference<T : Any>(referred: T) {
    fun get(): T?
}

expect class AtomicBoolean(referred: Boolean) {
    fun get(): Boolean
    fun compareAndSet(expect: Boolean, update: Boolean): Boolean
}

expect class LockObject()

internal expect inline fun <R> synchronized(lock: LockObject, block: () -> R): R

internal expect suspend fun ByteReadChannel.source(): BufferedSource

internal expect val ioDispatcher: CoroutineDispatcher

internal expect val httpEngine: HttpClientEngine

internal expect val defaultFileSystem: FileSystem?

internal val defaultImageScope: CoroutineScope
    get() = CoroutineScope(SupervisorJob() + Dispatchers.Main)

internal val httpEngineFactory: () -> HttpClient
    get() = { HttpClient(httpEngine) }
