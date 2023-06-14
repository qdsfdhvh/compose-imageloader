package com.seiko.imageloader.util

import io.ktor.util.toByteArray
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.cancel
import okio.Buffer
import okio.BufferedSource

// TODO
internal actual suspend fun ByteReadChannel.source(): BufferedSource {
    return Buffer().apply {
        write(toByteArray())
        this@source.cancel()
    }
}
