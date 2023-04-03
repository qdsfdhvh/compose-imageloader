package com.seiko.imageloader.intercept

import com.seiko.imageloader.cache.disk.DiskCache
import com.seiko.imageloader.model.DataSource
import com.seiko.imageloader.model.ImageRequestEvent
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.util.closeQuietly
import com.seiko.imageloader.util.d
import com.seiko.imageloader.util.w
import okio.BufferedSource
import okio.buffer

class DiskCacheInterceptor(
    diskCache: () -> DiskCache,
) : Interceptor {

    private val diskCache by lazy(diskCache)

    private val fileSystem get() = diskCache.fileSystem

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val request = chain.request
        val options = chain.options
        val logger = chain.config.logger

        val cacheKey = chain.components.key(request.data, options)
            ?: return chain.proceed(request)

        var snapshot = runCatching {
            readFromDiskCache(options, cacheKey)
        }.onFailure {
            logger.w(
                tag = "DiskCacheInterceptor",
                data = request.data,
                throwable = it,
            ) { "read disk cache error:" }
        }.getOrNull()

        request.call(ImageRequestEvent.ReadDiskCache(snapshot != null))
        if (snapshot != null) {
            logger.d(
                tag = "DiskCacheInterceptor",
                data = request.data,
            ) { "read disk cache" }
            return ImageResult.Source(
                request = request,
                source = snapshot.source(),
                dataSource = DataSource.Disk,
            )
        }

        val result = chain.proceed(request)
        when (result) {
            is ImageResult.Source -> {
                snapshot = runCatching {
                    writeToDiskCache(
                        options,
                        cacheKey,
                        snapshot,
                        result.source,
                    )
                }.onFailure {
                    logger.w(
                        tag = "DiskCacheInterceptor",
                        data = request.data,
                        throwable = it,
                    ) { "write disk cache error:" }
                }.getOrNull()
                if (snapshot != null) {
                    logger.d(
                        tag = "DiskCacheInterceptor",
                        data = request.data,
                    ) { "write disk cache" }
                    return result.copy(
                        source = snapshot.source(),
                    )
                }
            }
            else -> Unit
        }
        return result
    }

    private fun readFromDiskCache(
        options: Options,
        cacheKey: String,
    ): DiskCache.Snapshot? {
        return if (options.diskCachePolicy.readEnabled) {
            diskCache[cacheKey]
        } else {
            null
        }
    }

    private fun writeToDiskCache(
        options: Options,
        cacheKey: String,
        snapshot: DiskCache.Snapshot?,
        source: BufferedSource,
    ): DiskCache.Snapshot? {
        if (!options.diskCachePolicy.writeEnabled) {
            snapshot?.closeQuietly()
            return null
        }
        val editor = snapshot?.closeAndEdit()
            ?: diskCache.edit(cacheKey)
            ?: return null
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
