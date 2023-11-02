package com.seiko.imageloader.util

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import okio.FileSystem

actual typealias WeakReference<T> = kotlin.native.ref.WeakReference<T>

internal actual val httpEngine: HttpClientEngine get() = Darwin.create()

internal actual val defaultFileSystem: FileSystem? get() = FileSystem.SYSTEM
