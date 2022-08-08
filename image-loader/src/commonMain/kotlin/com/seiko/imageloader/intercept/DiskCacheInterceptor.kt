package com.seiko.imageloader.intercept

import com.seiko.imageloader.cache.disk.DiskCache
import com.seiko.imageloader.request.ImageResult
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.request.SourceResult
import com.seiko.imageloader.util.closeQuietly
import io.github.aakira.napier.Napier
import okio.BufferedSource
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
            Napier.d(tag = "Interceptor") { "read disk cache, data:$data" }
            return SourceResult(
                request = request,
                channel = snapshot.source(),
                mimeType = null,
                metadata = null,
            )
        }

        val result = chain.proceed(request)
        when (result) {
            is SourceResult -> {
                snapshot = writeToDiskCache(options, cacheKey, snapshot, result.channel)
                if (snapshot != null) {
                    return SourceResult(
                        request = request,
                        channel = snapshot.source(),
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
            runCatching {
                diskCache.value[cacheKey]
            }.onFailure {
                Napier.d(tag = "DiskCacheInterceptor", throwable = it) { "fail read disk cache error:" }
            }.getOrNull()
        } else null
    }

    private fun writeToDiskCache(
        options: Options,
        cacheKey: String,
        snapshot: DiskCache.Snapshot?,
        source: BufferedSource,
    ): DiskCache.Snapshot? {
        if (!options.diskCachePolicy.writeEnabled) {
            snapshot?.close()
            return null
        }
        val editor = snapshot?.closeAndEdit() ?: diskCache.value.edit(cacheKey) ?: return null
        try {
            fileSystem.write(editor.data) {
                writeAll(source)
            }
            return editor.commitAndGet()
        } catch (e: Exception) {
            editor.abortQuietly()
            throw e
        } finally {
            source.closeQuietly()
        }
    }

    private fun DiskCache.Snapshot.source(): BufferedSource {
        return fileSystem.source(data).buffer()
    }
}

private fun DiskCache.Editor.abortQuietly() {
    try {
        abort()
    } catch (_: Exception) {}
}
