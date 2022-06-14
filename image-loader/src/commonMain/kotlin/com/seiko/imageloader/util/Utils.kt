package com.seiko.imageloader.util

import io.ktor.utils.io.ByteReadChannel
import okio.Sink
import okio.Source

internal expect val Any.identityHashCode: Int

internal expect fun Source.toByteReadChannel(): ByteReadChannel

internal expect fun ByteReadChannel.toSource(): Source

internal expect suspend fun ByteReadChannel.copyTo(sink: Sink): Long
