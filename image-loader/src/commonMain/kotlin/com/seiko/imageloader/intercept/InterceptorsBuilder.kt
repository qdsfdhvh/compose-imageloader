package com.seiko.imageloader.intercept

import com.seiko.imageloader.cache.disk.DiskCache
import com.seiko.imageloader.cache.memory.MemoryCache

class InterceptorsBuilder {

    private val interceptors = mutableListOf<Interceptor>()
    private var memoryCache: Lazy<MemoryCache>? = null
    private var diskCache: Lazy<DiskCache>? = null

    fun addInterceptor(interceptor: Interceptor) = apply {
        interceptors.add(interceptor)
    }

    fun memoryCache(initializer: (() -> MemoryCache)?) = apply {
        memoryCache = initializer?.let { lazy(it) }
    }

    fun diskCache(initializer: (() -> DiskCache)?) = apply {
        diskCache = initializer?.let { lazy(it) }
    }

    fun build(): List<Interceptor> {
        return interceptors + listOfNotNull(
            MappedInterceptor(),
            memoryCache?.let { MemoryCacheInterceptor(it) },
            DecodeInterceptor(),
            diskCache?.let { DiskCacheInterceptor(it) },
            FetchInterceptor(),
        )
    }
}
