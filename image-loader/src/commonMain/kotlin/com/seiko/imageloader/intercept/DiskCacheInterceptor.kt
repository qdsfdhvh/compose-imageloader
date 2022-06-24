package com.seiko.imageloader.intercept

import com.seiko.imageloader.cache.disk.DiskCache
import com.seiko.imageloader.request.ImageResult
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.request.SourceResult
import com.seiko.imageloader.util.saveTo
import com.seiko.imageloader.util.toByteReadChannel
import io.github.aakira.napier.Napier
import io.ktor.utils.io.ByteReadChannel
import okio.buffer

class DiskCacheInterceptor(
    private val diskCache: Lazy<DiskCache>,
) : Interceptor {

    private val fileSystem get() = diskCache.value.fileSystem

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val (request, options, components) = chain

        val data = request.data
        val cacheKey = components.key(data, options) ?: return chain.proceed(request)

        var snapshot = readFromDiskCache(options, cacheKey)
        if (snapshot != null) {
            return SourceResult(
                request = request,
                channel = snapshot.toByteReadChannel(),
                mimeType = null,
                metadata = null,
            )
        }

        val result = chain.proceed(request)
        when (result) {
            is SourceResult -> {
                Napier.d { "get image from network" }
                snapshot = writeToDiskCache(snapshot, cacheKey, result.channel)
                if (snapshot != null && options.diskCachePolicy.writeEnabled) {
                    return SourceResult(
                        request = request,
                        channel = snapshot.toByteReadChannel(),
                        mimeType = result.mimeType,
                        metadata = result.metadata,
                    )
                }
            }
            else -> Unit
        }
        return result
    }

    private fun readFromDiskCache(options: Options, cacheKey: String): DiskCache.Snapshot? {
        return if (options.diskCachePolicy.readEnabled) {
            diskCache.value[cacheKey]
        } else null
    }

    private suspend fun writeToDiskCache(
        snapshot: DiskCache.Snapshot?,
        cacheKey: String,
        channel: ByteReadChannel,
    ): DiskCache.Snapshot? {
        val editor = snapshot?.closeAndEdit() ?: diskCache.value.edit(cacheKey) ?: return null
        try {
            channel.saveTo(editor.data, fileSystem)
            return editor.commitAndGet()
        } catch (e: Exception) {
            editor.abortQuietly()
            throw e
        }
    }

    private fun DiskCache.Snapshot.toByteReadChannel(): ByteReadChannel {
        return fileSystem.source(data).buffer().toByteReadChannel()
    }
}

private fun DiskCache.Editor.abortQuietly() {
    try {
        abort()
    } catch (_: Exception) {}
}
