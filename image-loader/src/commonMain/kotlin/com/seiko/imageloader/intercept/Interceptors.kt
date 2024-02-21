package com.seiko.imageloader.intercept

import com.seiko.imageloader.Bitmap
import com.seiko.imageloader.cache.disk.DiskCache
import com.seiko.imageloader.cache.disk.DiskCacheBuilder
import com.seiko.imageloader.cache.memory.MemoryCache
import com.seiko.imageloader.cache.memory.MemoryCacheBuilder
import com.seiko.imageloader.cache.memory.MemoryKey
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.size
import com.seiko.imageloader.util.defaultFileSystem
import com.seiko.imageloader.util.identityHashCode
import okio.FileSystem

class Interceptors internal constructor(
    internal val useDefaultInterceptors: Boolean,
    internal val interceptorList: List<Interceptor>,
    internal val defaultMemoryCacheInterceptorList: Set<MemoryCacheInterceptor<*>>,
    internal val diskCache: (() -> DiskCache)?,
) {
    val list: List<Interceptor> by lazy {
        if (useDefaultInterceptors) {
            buildList {
                addAll(interceptorList)
                add(MappedInterceptor())
                addAll(defaultMemoryCacheInterceptorList)
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
    private val defaultMemoryCacheInterceptorList = mutableSetOf<MemoryCacheInterceptor<*>>()
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
            defaultMemoryCacheInterceptorList.clear()
        }
        interceptorList.addAll(interceptors.interceptorList)
        defaultMemoryCacheInterceptorList.addAll(interceptors.defaultMemoryCacheInterceptorList)
        diskCache = interceptors.diskCache
    }

    fun addInterceptor(interceptor: Interceptor) {
        interceptorList.add(interceptor)
    }

    fun addInterceptors(interceptors: Collection<Interceptor>) {
        interceptorList.addAll(interceptors)
    }

    /**
     * only use for useDefaultInterceptors = true
     */
    fun addDefaultMemoryCacheInterceptor(interceptor: MemoryCacheInterceptor<*>) {
        defaultMemoryCacheInterceptorList.add(interceptor)
    }

    @Deprecated(
        message = "Use addMemoryCacheInterceptor instead",
        replaceWith = ReplaceWith("addMemoryCacheInterceptor(memoryInterceptor(mapToMemoryValue = mapToMemoryValue, mapToImageResult = mapToImageResult, memoryCache = block))"),
    )
    fun <T : Any> anyMemoryCache(
        mapToMemoryValue: (ImageResult) -> T?,
        mapToImageResult: (T) -> ImageResult?,
        block: () -> MemoryCache<MemoryKey, T>,
    ) {
        addDefaultMemoryCacheInterceptor(
            memoryInterceptor(
                mapToMemoryValue = mapToMemoryValue,
                mapToImageResult = mapToImageResult,
                memoryCache = block,
            ),
        )
    }

    @Deprecated(
        message = "Use bitmapMemoryCacheConfig instead",
        replaceWith = ReplaceWith("bitmapMemoryCacheConfig(valueHashProvider = valueHashProvider, valueSizeProvider = valueSizeProvider, block = block)"),
    )
    fun memoryCacheConfig(
        valueHashProvider: (Bitmap) -> Int = { identityHashCode(it) },
        valueSizeProvider: (Bitmap) -> Int = { it.size },
        block: MemoryCacheBuilder<MemoryKey, Bitmap>.() -> Unit,
    ) {
        bitmapMemoryCacheConfig(
            valueHashProvider = valueHashProvider,
            valueSizeProvider = valueSizeProvider,
            block = block,
        )
    }

    @Deprecated(
        message = "Use bitmapMemoryCache instead",
        replaceWith = ReplaceWith("bitmapMemoryCache(memoryCache = memoryCache, mapToMemoryValue = mapToMemoryValue, mapToImageResult = mapToImageResult)"),
    )
    fun memoryCache(
        mapToMemoryValue: (ImageResult) -> Bitmap? = { (it as? ImageResult.OfBitmap)?.bitmap },
        mapToImageResult: (Bitmap) -> ImageResult? = { ImageResult.OfBitmap(it) },
        memoryCache: () -> MemoryCache<MemoryKey, Bitmap>,
    ) {
        bitmapMemoryCache(
            memoryCache = memoryCache,
            mapToMemoryValue = mapToMemoryValue,
            mapToImageResult = mapToImageResult,
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
            defaultMemoryCacheInterceptorList = defaultMemoryCacheInterceptorList,
            diskCache = diskCache,
        )
    }
}
