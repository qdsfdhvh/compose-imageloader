package com.seiko.imageloader.intercept

import com.seiko.imageloader.Bitmap
import com.seiko.imageloader.cache.memory.MemoryCache
import com.seiko.imageloader.cache.memory.MemoryKey
import com.seiko.imageloader.cache.memory.MemoryValue
import com.seiko.imageloader.model.ImageRequestEvent
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.util.d
import com.seiko.imageloader.util.w

class MemoryCacheInterceptor(
    memoryCache: () -> MemoryCache,
) : Interceptor {

    private val memoryCache by lazy(memoryCache)

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val request = chain.request
        val options = chain.options
        val logger = chain.config.logger

        val cacheKey = chain.components.key(request.data, options)
            ?: return chain.proceed(request)

        val memoryCacheValue = runCatching {
            readFromMemoryCache(options, cacheKey)
        }.onFailure {
            logger.w(
                tag = "MemoryCacheInterceptor",
                data = request.data,
                throwable = it,
            ) { "read memory cache error:" }
        }.getOrNull()

        request.call(ImageRequestEvent.ReadMemoryCache(memoryCacheValue != null))
        if (memoryCacheValue != null) {
            logger.d(
                tag = "MemoryCacheInterceptor",
                data = request.data,
            ) { "read memory cache." }
            return ImageResult.Bitmap(
                request = request,
                bitmap = memoryCacheValue,
            )
        }

        val result = chain.proceed(request)
        when (result) {
            is ImageResult.Bitmap -> {
                runCatching {
                    writeToMemoryCache(options, cacheKey, result.bitmap)
                }.onFailure {
                    logger.w(
                        tag = "MemoryCacheInterceptor",
                        data = request.data,
                        throwable = it,
                    ) { "write memory cache error:" }
                }.onSuccess { success ->
                    if (success) {
                        logger.d(
                            tag = "MemoryCacheInterceptor",
                            data = request.data,
                        ) { "write memory cache." }
                    }
                }
            }
            else -> Unit
        }
        return result
    }

    private fun readFromMemoryCache(options: Options, cacheKey: MemoryKey): MemoryValue? {
        return if (options.memoryCachePolicy.readEnabled) {
            memoryCache[cacheKey]
        } else {
            null
        }
    }

    private fun writeToMemoryCache(
        options: Options,
        cacheKey: MemoryKey,
        image: Bitmap,
    ): Boolean {
        if (!options.memoryCachePolicy.writeEnabled) return false
        memoryCache[cacheKey] = image
        return true
    }
}
