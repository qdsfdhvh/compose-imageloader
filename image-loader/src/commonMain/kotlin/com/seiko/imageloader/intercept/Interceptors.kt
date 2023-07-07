package com.seiko.imageloader.intercept

import com.seiko.imageloader.cache.disk.DiskCache
import com.seiko.imageloader.cache.disk.DiskCacheBuilder
import com.seiko.imageloader.cache.memory.MemoryCache
import com.seiko.imageloader.cache.memory.MemoryCacheBuilder
import com.seiko.imageloader.cache.memory.MemoryKey
import com.seiko.imageloader.cache.memory.MemoryValue
import com.seiko.imageloader.identityHashCode
import com.seiko.imageloader.size
import com.seiko.imageloader.util.defaultFileSystem
import okio.FileSystem

internal typealias Interceptors = List<Interceptor>

class InterceptorsBuilder internal constructor() {

    private val interceptors = mutableListOf<Interceptor>()
    private var memoryCache: (() -> MemoryCache<MemoryKey, MemoryValue>)? = null
    private var diskCache: (() -> DiskCache)? = null

    var useDefaultInterceptors = true

    fun addInterceptor(interceptor: Interceptor) {
        interceptors.add(interceptor)
    }

    fun addInterceptors(interceptors: Collection<Interceptor>) {
        this.interceptors.addAll(interceptors)
    }

    fun memoryCacheConfig(block: MemoryCacheBuilder<MemoryKey, MemoryValue>.() -> Unit) {
        memoryCache = {
            MemoryCache(
                valueHashProvider = { it.identityHashCode },
                valueSizeProvider = { it.size },
                block = block,
            )
        }
    }

    fun memoryCache(block: () -> MemoryCache<MemoryKey, MemoryValue>) {
        memoryCache = block
    }

    fun diskCacheConfig(
        fileSystem: FileSystem? = defaultFileSystem,
        block: DiskCacheBuilder.() -> Unit,
    ) {
        if (fileSystem != null) {
            diskCache = { DiskCache(fileSystem, block) }
        }
    }

    fun diskCache(block: () -> DiskCache) {
        diskCache = block
    }

    internal fun build(): Interceptors {
        return interceptors + if (useDefaultInterceptors) {
            listOfNotNull(
                MappedInterceptor(),
                memoryCache?.let { MemoryCacheInterceptor(it) },
                DecodeInterceptor(),
                diskCache?.let { DiskCacheInterceptor(it) },
                FetchInterceptor(),
            )
        } else {
            emptyList()
        }
    }
}
