package com.seiko.imageloader.intercept

import com.seiko.imageloader.cache.disk.DiskCache
import com.seiko.imageloader.request.DataSource
import com.seiko.imageloader.request.ImageResult
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.request.SourceResult
import com.seiko.imageloader.util.closeQuietly
import com.seiko.imageloader.util.logd
import com.seiko.imageloader.util.logi
import com.seiko.imageloader.util.logw
import okio.BufferedSource
import okio.buffer

class DiskCacheInterceptor(
    private val diskCache: Lazy<DiskCache>,
) : Interceptor {

    private val fileSystem get() = diskCache.value.fileSystem

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val cacheKey = chain.components.key(chain.request.data, chain.options)
            ?: return chain.proceed(chain.request)

        var snapshot = runCatching {
            readFromDiskCache(chain.options, cacheKey)
        }.onFailure {
            logw(
                tag = "DiskCacheInterceptor",
                data = chain.request.data,
                throwable = it,
            ) { "read disk cache error:" }
        }.getOrNull()
        if (snapshot != null) {
            logi(
                tag = "DiskCacheInterceptor",
                data = chain.request.data,
            ) { "read disk cache" }
            return SourceResult(
                request = chain.request,
                channel = snapshot.source(),
                dataSource = DataSource.Disk,
                mimeType = null,
                metadata = null,
            )
        }

        val result = chain.proceed(chain.request)
        when (result) {
            is SourceResult -> {
                snapshot = runCatching {
                    writeToDiskCache(
                        chain.options,
                        cacheKey,
                        snapshot,
                        result.channel,
                    )
                }.onFailure {
                    logw(
                        tag = "DiskCacheInterceptor",
                        data = chain.request.data,
                        throwable = it,
                    ) { "write disk cache error:" }
                }.getOrNull()
                if (snapshot != null) {
                    logd(
                        tag = "DiskCacheInterceptor",
                        data = chain.request.data,
                    ) { "write disk cache" }
                    return result.copy(
                        channel = snapshot.source(),
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
            diskCache.value[cacheKey]
        } else null
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
            ?: diskCache.value.edit(cacheKey)
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
