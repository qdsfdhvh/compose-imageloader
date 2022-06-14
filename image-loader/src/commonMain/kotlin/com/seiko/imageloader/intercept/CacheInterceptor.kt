package com.seiko.imageloader.intercept

import com.seiko.imageloader.cache.disk.DiskCache
import com.seiko.imageloader.cache.memory.MemoryCache
import com.seiko.imageloader.cache.memory.MemoryKey
import com.seiko.imageloader.cache.memory.MemoryValue
import com.seiko.imageloader.request.ImageResult
import com.seiko.imageloader.request.SourceResult
import com.seiko.imageloader.request.SuccessResult
import io.github.aakira.napier.Napier
import okio.buffer

// TODO make cache better
class CacheInterceptor(
    private val memoryCache: Lazy<MemoryCache>,
    private val diskCache: Lazy<DiskCache>?,
) : Interceptor {
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        Napier.d(tag = "Interceptor") { "intercept CacheInterceptor" }

        val request = chain.request
        val components = chain.components
        val options = chain.options

        val data = request.data
        val cacheKey = components.key(data, options) ?: return chain.proceed(request)

        Napier.d(tag = "Interceptor") { "cacheKey=$cacheKey" }
        val memoryCache = memoryCache.value
        val memoryCacheKey = MemoryKey(cacheKey)
        val memoryCacheValue = memoryCache[memoryCacheKey]
        if (memoryCacheValue != null) {
            Napier.d(tag = "Interceptor") { "read memory cache data:$data" }
            return SuccessResult(
                request = request,
                image = memoryCacheValue.image,
            )
        }

        if (diskCache != null) {
            Napier.d(tag = "Interceptor") { "try read disk cache" }
            val diskCache = diskCache.value
            val diskCacheValue = diskCache[cacheKey]
            Napier.d(tag = "Interceptor") { "disk cache value:$diskCacheValue" }
            if (diskCacheValue != null) {
                Napier.d(tag = "Interceptor") { "read disk cache data:$data" }
                return SourceResult(
                    request = request,
                    source = diskCache.fileSystem.source(diskCacheValue.data).buffer(),
                )
            }
        }

        val result = chain.proceed(request)
        when (result) {
            is SourceResult -> {
                diskCache?.value?.let { diskCache ->
                    diskCache.edit(cacheKey)?.let { edit ->
                        diskCache.fileSystem.write(edit.data) {
                            result.source.readAll(this)
                        }
                        edit.commitAndGet()?.let {
                            return SourceResult(
                                request = request,
                                source = diskCache.fileSystem.source(it.data).buffer(),
                                metadata = result.metadata,
                            )
                        }
                    }
                }
            }

            is SuccessResult -> {
                memoryCache[memoryCacheKey] = MemoryValue(
                    image = result.image,
                )
            }
        }
        return result
    }
}
