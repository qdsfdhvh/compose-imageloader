package com.seiko.imageloader.intercept

import com.seiko.imageloader.Image
import com.seiko.imageloader.cache.memory.MemoryCache
import com.seiko.imageloader.cache.memory.MemoryKey
import com.seiko.imageloader.cache.memory.MemoryValue
import com.seiko.imageloader.request.ComposeImageResult
import com.seiko.imageloader.request.ImageResult
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.util.logd
import com.seiko.imageloader.util.logi
import com.seiko.imageloader.util.logw

class MemoryCacheInterceptor(
    private val memoryCache: Lazy<MemoryCache>,
) : Interceptor {
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val cacheKey = chain.components.key(chain.request.data, chain.options)
            ?: return chain.proceed(chain.request)

        val memoryCacheValue = runCatching {
            readFromMemoryCache(chain.options, cacheKey)
        }.onFailure {
            logw(
                tag = "MemoryCacheInterceptor",
                data = chain.request.data,
                throwable = it
            ) { "read memory cache error:" }
        }.getOrNull()
        if (memoryCacheValue != null) {
            logi(
                tag = "MemoryCacheInterceptor",
                data = chain.request.data,
            ) { "read memory cache." }
            return ComposeImageResult(
                request = chain.request,
                image = memoryCacheValue,
            )
        }

        val result = chain.proceed(chain.request)
        when (result) {
            is ComposeImageResult -> {
                runCatching {
                    writeToMemoryCache(chain.options, cacheKey, result.image)
                }.onFailure {
                    logw(
                        tag = "MemoryCacheInterceptor",
                        data = chain.request.data,
                        throwable = it
                    ) { "write memory cache error:" }
                }.onSuccess { success ->
                    if (success) {
                        logd(
                            tag = "MemoryCacheInterceptor",
                            data = chain.request.data,
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
            memoryCache.value[cacheKey]
        } else null
    }

    private fun writeToMemoryCache(
        options: Options,
        cacheKey: MemoryKey,
        image: Image,
    ): Boolean {
        if (!options.memoryCachePolicy.writeEnabled) return false
        memoryCache.value[cacheKey] = image
        return true
    }
}
