package com.seiko.imageloader.intercept

import com.seiko.imageloader.cache.memory.MemoryCache
import com.seiko.imageloader.cache.memory.MemoryKey
import com.seiko.imageloader.cache.memory.MemoryValue
import com.seiko.imageloader.request.ImageResult
import com.seiko.imageloader.request.SuccessResult
import io.github.aakira.napier.Napier

class MemoryCacheInterceptor(
    private val memoryCache: Lazy<MemoryCache>,
) : Interceptor {
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val (request, options, components) = chain

        val data = request.data
        val cacheKey = components.key(data, options) ?: return chain.proceed(request)

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

        val result = chain.proceed(request)
        when (result) {
            is SuccessResult -> {
                memoryCache[memoryCacheKey] = MemoryValue(
                    image = result.image,
                )
            }
        }
        return result
    }
}
