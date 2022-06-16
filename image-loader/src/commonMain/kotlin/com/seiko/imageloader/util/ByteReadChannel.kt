package com.seiko.imageloader.util

import io.ktor.utils.io.ByteReadChannel
import okio.ByteString

internal expect suspend fun ByteReadChannel.rangeEquals(offset: Long, bytes: ByteString): Boolean

internal expect suspend fun ByteReadChannel.request(byteCount: Int): Boolean

internal expect suspend fun ByteReadChannel.buffer(index: Int): Byte
