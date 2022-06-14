package com.seiko.imageloader.intercept

import com.seiko.imageloader.cache.disk.DiskCache
import com.seiko.imageloader.cache.memory.MemoryCache
import com.seiko.imageloader.request.ImageResult
import io.github.aakira.napier.Napier

class CacheInterceptor(
    private val memoryCache: Lazy<MemoryCache>,
    private val diskCache: Lazy<DiskCache>,
) : Interceptor {
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        Napier.d(tag = "Interceptor") { "intercept CacheInterceptor" }

        val request = chain.request
        val components = chain.components
        val options = chain.options

        // val data = request.data
        // val memoryCacheKey = components.key(data, options)
        // Napier.d(tag = "Interceptor") { "memoryCacheKey= $memoryCacheKey" }
        //
        //
        // val diskCacheKey = memoryCacheKey ?: data.toString()
        // Napier.d(tag = "Interceptor") { "diskCacheKey= $diskCacheKey" }
        //
        // val diskCacheValue = diskCache.value[diskCacheKey]
        // if (diskCacheValue != null) {
        //
        // }

        return chain.proceed(request)
    }
}