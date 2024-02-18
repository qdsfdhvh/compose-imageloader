package com.seiko.imageloader.intercept

import androidx.compose.ui.graphics.painter.Painter
import com.seiko.imageloader.Bitmap
import com.seiko.imageloader.Image
import com.seiko.imageloader.cache.memory.MemoryCache
import com.seiko.imageloader.cache.memory.MemoryCacheBuilder
import com.seiko.imageloader.cache.memory.MemoryKey
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.size
import com.seiko.imageloader.util.identityHashCode

fun <T> memoryInterceptor(
    memoryCache: () -> MemoryCache<MemoryKey, T>,
    mapToMemoryValue: (ImageResult) -> T?,
    mapToImageResult: (T) -> ImageResult?,
) = MemoryCacheInterceptor(
    memoryCache = memoryCache,
    mapToMemoryValue = mapToMemoryValue,
    mapToImageResult = mapToImageResult,
)

fun bitmapMemoryInterceptor(
    valueHashProvider: (Bitmap) -> Int = { identityHashCode(it) },
    valueSizeProvider: (Bitmap) -> Int = { it.size },
    block: MemoryCacheBuilder<MemoryKey, Bitmap>.() -> Unit,
) = memoryInterceptor(
    memoryCache = {
        MemoryCache(
            valueHashProvider = valueHashProvider,
            valueSizeProvider = valueSizeProvider,
            block = block,
        )
    },
    mapToMemoryValue = { (it as? ImageResult.OfBitmap)?.bitmap },
    mapToImageResult = { ImageResult.OfBitmap(it) },
)

fun imageMemoryInterceptor(
    valueHashProvider: (Image) -> Int = { identityHashCode(it) },
    valueSizeProvider: (Image) -> Int = { 1 },
    block: MemoryCacheBuilder<MemoryKey, Image>.() -> Unit,
) = memoryInterceptor(
    memoryCache = {
        MemoryCache(
            valueHashProvider = valueHashProvider,
            valueSizeProvider = valueSizeProvider,
            block = block,
        )
    },
    mapToMemoryValue = { (it as? ImageResult.OfImage)?.image },
    mapToImageResult = { ImageResult.OfImage(it) },
)

fun painterMemoryInterceptor(
    valueHashProvider: (Painter) -> Int = { identityHashCode(it) },
    valueSizeProvider: (Painter) -> Int = { 1 },
    block: MemoryCacheBuilder<MemoryKey, Painter>.() -> Unit,
) = memoryInterceptor(
    memoryCache = {
        MemoryCache(
            valueHashProvider = valueHashProvider,
            valueSizeProvider = valueSizeProvider,
            block = block,
        )
    },
    mapToMemoryValue = { (it as? ImageResult.OfPainter)?.painter },
    mapToImageResult = { ImageResult.OfPainter(it) },
)
