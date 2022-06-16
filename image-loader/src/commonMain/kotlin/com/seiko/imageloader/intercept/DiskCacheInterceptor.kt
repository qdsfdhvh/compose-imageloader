package com.seiko.imageloader.intercept

import com.seiko.imageloader.cache.disk.DiskCache
import com.seiko.imageloader.request.ImageResult
import com.seiko.imageloader.request.SourceResult
import com.seiko.imageloader.util.saveTo
import com.seiko.imageloader.util.toByteReadChannel
import io.github.aakira.napier.Napier
import okio.buffer

class DiskCacheInterceptor(
    private val diskCache: Lazy<DiskCache>,
) : Interceptor {
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val (request, options, components) = chain

        val data = request.data
        val cacheKey = components.key(data, options) ?: return chain.proceed(request)

        val diskCache = diskCache.value
        val diskCacheValue = diskCache[cacheKey]
        if (diskCacheValue != null) {
            Napier.d(tag = "Interceptor") { "read disk cache data:$data" }
            return SourceResult(
                request = request,
                channel = diskCache.fileSystem.source(diskCacheValue.data).buffer().toByteReadChannel(),
            )
        }

        val result = chain.proceed(request)
        when (result) {
            is SourceResult -> {
                diskCache.edit(cacheKey)?.let { edit ->
                    result.channel.saveTo(edit.data, diskCache.fileSystem)
                    edit.commitAndGet()?.let {
                        SourceResult(
                            request = request,
                            channel = diskCache.fileSystem.source(it.data).buffer().toByteReadChannel(),
                            mimeType = result.mimeType,
                            metadata = result.metadata,
                        )
                    }
                }
            }
        }
        return result
    }
}
