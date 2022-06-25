package com.seiko.imageloader.util

import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.pool.ByteArrayPool
import io.ktor.utils.io.pool.ObjectPool
import okio.ByteString
import okio.FileSystem
import okio.Path
import okio.buffer
import okio.use

internal expect suspend fun ByteReadChannel.rangeEquals(offset: Long, bytes: ByteString): Boolean

internal expect suspend fun ByteReadChannel.request(byteCount: Int): Boolean

internal expect suspend fun ByteReadChannel.buffer(index: Int): Byte

internal suspend fun ByteReadChannel.saveTo(
    path: Path,
    fileSystem: FileSystem,
    pool: ObjectPool<ByteArray> = ByteArrayPool,
) {
    val buffer = pool.borrow()
    fileSystem.sink(path).buffer().use { sink ->
        try {
            while (true) {
                val readCount = readAvailable(buffer, 0, buffer.size)
                if (readCount < 0) break
                if (readCount == 0) continue
                sink.write(buffer, 0, readCount)
            }
        } catch (cause: Throwable) {
            cause.printStackTrace()
        } finally {
            pool.recycle(buffer)
        }
    }
}
