package com.seiko.imageloader.intercept

import com.seiko.imageloader.cache.disk.DiskCache
import com.seiko.imageloader.cache.memory.MemoryCache

class InterceptorsBuilder {

    private val interceptors = mutableListOf<Interceptor>()
    private var memoryCache: Lazy<MemoryCache>? = null
    private var diskCache: Lazy<DiskCache>? = null

    var useDefaultInterceptors = true

    fun addInterceptor(interceptor: Interceptor) {
        interceptors.add(interceptor)
    }

    fun addInterceptors(interceptors: Collection<Interceptor>) {
        this.interceptors.addAll(interceptors)
    }

    fun memoryCache(initializer: (() -> MemoryCache)?) {
        memoryCache = initializer?.let { lazy(it) }
    }

    fun diskCache(initializer: (() -> DiskCache)?) {
        diskCache = initializer?.let { lazy(it) }
    }

    fun build(): List<Interceptor> {
        return interceptors + if (useDefaultInterceptors) {
            listOfNotNull(
                MappedInterceptor(),
                memoryCache?.let { MemoryCacheInterceptor(it) },
                DecodeInterceptor(),
                diskCache?.let { DiskCacheInterceptor(it) },
                FetchInterceptor(),
            )
        } else emptyList()
    }
}
