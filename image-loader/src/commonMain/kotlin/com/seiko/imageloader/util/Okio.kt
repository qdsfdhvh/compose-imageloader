package com.seiko.imageloader.util

import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.pool.ByteArrayPool
import io.ktor.utils.io.pool.ObjectPool
import io.ktor.utils.io.writer
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import okio.BufferedSource
import kotlin.coroutines.CoroutineContext

@OptIn(DelicateCoroutinesApi::class)
internal fun BufferedSource.toByteReadChannel(
    context: CoroutineContext = Dispatchers.Default,
    pool: ObjectPool<ByteArray> = ByteArrayPool,
): ByteReadChannel = GlobalScope.writer(context, autoFlush = true) {
    val buffer = pool.borrow()
    try {
        while (true) {
            val readCount = read(buffer, 0, buffer.size)
            if (readCount < 0) break
            if (readCount == 0) continue
            channel.writeFully(buffer, 0, readCount)
        }
    } catch (cause: Throwable) {
        channel.close(cause)
    } finally {
        pool.recycle(buffer)
        close()
    }
}.channel
