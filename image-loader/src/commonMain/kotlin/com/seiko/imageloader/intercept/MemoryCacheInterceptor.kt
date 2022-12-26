package com.seiko.imageloader.intercept

import com.seiko.imageloader.Image
import com.seiko.imageloader.cache.memory.MemoryCache
import com.seiko.imageloader.cache.memory.MemoryKey
import com.seiko.imageloader.cache.memory.MemoryValue
import com.seiko.imageloader.request.ComposeImageResult
import com.seiko.imageloader.request.ImageResult
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.util.log
import com.seiko.imageloader.util.parseString

class MemoryCacheInterceptor(
    private val memoryCache: Lazy<MemoryCache>,
) : Interceptor {
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val (request, options, components) = chain

        val data = request.data
        val cacheKey = components.key(data, options) ?: return chain.proceed(request)

        val memoryCacheValue = readFromMemoryCache(options, cacheKey)
        if (memoryCacheValue != null) {
            log(tag = "MemoryCacheInterceptor") { "read memory cache, data:${data.parseString()}" }
            return ComposeImageResult(
                request = request,
                image = memoryCacheValue,
            )
        }

        val result = chain.proceed(request)
        when (result) {
            is ComposeImageResult -> {
                writeToMemoryCache(options, cacheKey, result.image)
            }
            else -> Unit
        }
        return result
    }

    private fun readFromMemoryCache(options: Options, cacheKey: MemoryKey): MemoryValue? {
        return if (options.memoryCachePolicy.readEnabled) {
            runCatching {
                memoryCache.value[cacheKey]
            }.onFailure {
                log(tag = "MemoryCacheInterceptor", throwable = it) { "fail read disk cache error:" }
            }.getOrNull()
        } else null
    }

    private fun writeToMemoryCache(
        options: Options,
        cacheKey: MemoryKey,
        image: Image,
    ) {
        if (!options.memoryCachePolicy.writeEnabled) return
        memoryCache.value[cacheKey] = image
    }
}
