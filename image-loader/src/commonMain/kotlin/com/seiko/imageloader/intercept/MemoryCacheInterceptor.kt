package com.seiko.imageloader.intercept

import androidx.compose.ui.geometry.isSpecified
import com.seiko.imageloader.BitmapConfig
import com.seiko.imageloader.cache.memory.MemoryCache
import com.seiko.imageloader.cache.memory.MemoryKey
import com.seiko.imageloader.model.ImageEvent
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.option.Scale
import com.seiko.imageloader.util.d
import com.seiko.imageloader.util.w

class MemoryCacheInterceptor<T>(
    memoryCache: () -> MemoryCache<MemoryKey, T>,
    private val mapToMemoryValue: (ImageResult) -> T?,
    private val mapToImageResult: (T) -> ImageResult?,
) : Interceptor {

    private val bitmapMemoryCache by lazy(memoryCache)

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val request = chain.request
        val options = chain.options
        val logger = chain.logger

        val key = chain.components.key(request.data, options)
            ?: return chain.proceed(request)

        val cacheKey = MemoryKey(
            key = key,
            extra = options.toMemoryExtra(),
        )

        val cachedImageResult = runCatching {
            readCachedValue(options, cacheKey)
        }.onFailure {
            logger.w(
                tag = "MemoryCacheInterceptor",
                data = request.data,
                throwable = it,
            ) { "read memory cache error:" }
        }.getOrNull()
        if (!request.skipEvent) {
            chain.emit(ImageEvent.StartWithMemory)
        }
        if (cachedImageResult != null) {
            logger.d(
                tag = "MemoryCacheInterceptor",
                data = request.data,
            ) { "read memory cache." }
            return cachedImageResult
        }

        val result = chain.proceed(request)
        runCatching {
            writeValueToMemory(options, cacheKey, result)
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
        return result
    }

    private fun readCachedValue(options: Options, cacheKey: MemoryKey): ImageResult? {
        return if (options.memoryCachePolicy.readEnabled) {
            bitmapMemoryCache[cacheKey]?.let(mapToImageResult)
        } else {
            null
        }
    }

    private fun writeValueToMemory(
        options: Options,
        cacheKey: MemoryKey,
        imageResult: ImageResult,
    ): Boolean {
        return if (options.memoryCachePolicy.writeEnabled) {
            mapToMemoryValue(imageResult)?.let {
                bitmapMemoryCache[cacheKey] = it
                true
            } ?: false
        } else {
            false
        }
    }

    private fun Options.toMemoryExtra(): Map<String, String> {
        return buildMap {
            if (allowInexactSize) {
                put("op#allowInexactSize", "true")
            }
            if (!premultipliedAlpha) {
                put("op#premultipliedAlpha", "false")
            }
            if (bitmapConfig != BitmapConfig.Default) {
                put("op#imageConfig", bitmapConfig.name)
            }
            if (this@toMemoryExtra.size.isSpecified) {
                put("op#size", this@toMemoryExtra.size.toString())
            }
            if (scale != Scale.FILL) {
                put("op#scale", scale.name)
            }
            if (!playAnimate) {
                put("op#noPlay", "true")
            }
            if (isBitmap) {
                put("op#isBitmap", "true")
            }
            if (repeatCount != Options.REPEAT_INFINITE) {
                put("op#repeatCount", repeatCount.toString())
            }
            if (maxImageSize != Options.DEFAULT_MAX_IMAGE_SIZE) {
                put("op#maxSize", maxImageSize.toString())
            }
            if (memoryCacheKeyExtras.isNotEmpty()) {
                putAll(memoryCacheKeyExtras)
            }
        }
    }
}
