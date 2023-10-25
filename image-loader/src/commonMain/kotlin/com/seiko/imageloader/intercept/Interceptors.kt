package com.seiko.imageloader.intercept

import com.seiko.imageloader.Bitmap
import com.seiko.imageloader.cache.disk.DiskCache
import com.seiko.imageloader.cache.disk.DiskCacheBuilder
import com.seiko.imageloader.cache.memory.MemoryCache
import com.seiko.imageloader.cache.memory.MemoryCacheBuilder
import com.seiko.imageloader.cache.memory.MemoryKey
import com.seiko.imageloader.identityHashCode
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.size
import com.seiko.imageloader.util.defaultFileSystem
import com.seiko.imageloader.util.forEachIndices
import okio.FileSystem

class Interceptors internal constructor(
    internal val useDefaultInterceptors: Boolean,
    internal val interceptorList: List<Interceptor>,
    internal val memoryCaches: List<MemoryCacheWrapper<*>>,
    internal val diskCache: (() -> DiskCache)?,
) {
    val list: List<Interceptor> by lazy {
        if (useDefaultInterceptors) {
            interceptorList + buildList {
                add(MappedInterceptor())
                memoryCaches.forEachIndices { wrapper ->
                    add(wrapper.toInterceptor())
                }
                add(DecodeInterceptor())
                diskCache?.let {
                    add(DiskCacheInterceptor(it))
                }
                add(FetchInterceptor())
            }
        } else {
            interceptorList
        }
    }
}

class InterceptorsBuilder internal constructor() {

    private val interceptorList = mutableListOf<Interceptor>()
    private val memoryCaches = mutableListOf<MemoryCacheWrapper<*>>()
    private var diskCache: (() -> DiskCache)? = null

    var useDefaultInterceptors = true

    fun takeFrom(
        interceptors: Interceptors,
        clearInterceptors: Boolean = false,
        clearMemoryCaches: Boolean = false,
    ) {
        useDefaultInterceptors = interceptors.useDefaultInterceptors
        if (clearInterceptors) {
            interceptorList.clear()
        }
        if (clearMemoryCaches) {
            memoryCaches.clear()
        }
        interceptorList.addAll(interceptors.interceptorList)
        memoryCaches.addAll(interceptors.memoryCaches)
        diskCache = interceptors.diskCache
    }

    fun addInterceptor(block: suspend (chain: Interceptor.Chain) -> ImageResult) {
        interceptorList.add(Interceptor(block))
    }

    fun addInterceptor(interceptor: Interceptor) {
        interceptorList.add(interceptor)
    }

    fun addInterceptors(interceptors: Collection<Interceptor>) {
        interceptorList.addAll(interceptors)
    }

    fun memoryCacheConfig(
        valueHashProvider: (Bitmap) -> Int = { it.identityHashCode },
        valueSizeProvider: (Bitmap) -> Int = { it.size },
        block: MemoryCacheBuilder<MemoryKey, Bitmap>.() -> Unit,
    ) {
        memoryCache(
            block = {
                MemoryCache(
                    valueHashProvider = valueHashProvider,
                    valueSizeProvider = valueSizeProvider,
                    block = block,
                )
            },
        )
    }

    fun memoryCache(
        mapToMemoryValue: (ImageResult) -> Bitmap? = { (it as? ImageResult.Bitmap)?.bitmap },
        mapToImageResult: (Bitmap) -> ImageResult? = { ImageResult.Bitmap(it) },
        block: () -> MemoryCache<MemoryKey, Bitmap>,
    ) {
        memoryCaches.add(
            MemoryCacheWrapper(
                memoryCache = block,
                mapToMemoryValue = mapToMemoryValue,
                mapToImageResult = mapToImageResult,
            ),
        )
    }

    fun <T : Any> anyMemoryCache(
        mapToMemoryValue: (ImageResult) -> T?,
        mapToImageResult: (T) -> ImageResult?,
        block: () -> MemoryCache<MemoryKey, T>,
    ) {
        memoryCaches.add(
            MemoryCacheWrapper(
                memoryCache = block,
                mapToMemoryValue = mapToMemoryValue,
                mapToImageResult = mapToImageResult,
            ),
        )
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
        return Interceptors(
            useDefaultInterceptors = useDefaultInterceptors,
            interceptorList = interceptorList,
            memoryCaches = memoryCaches,
            diskCache = diskCache,
        )
    }
}

internal class MemoryCacheWrapper<T>(
    val memoryCache: () -> MemoryCache<MemoryKey, T>,
    val mapToMemoryValue: (ImageResult) -> T?,
    val mapToImageResult: (T) -> ImageResult?,
) {
    fun toInterceptor() = MemoryCacheInterceptor(
        memoryCache = memoryCache,
        mapToMemoryValue = mapToMemoryValue,
        mapToImageResult = mapToImageResult,
    )
}
