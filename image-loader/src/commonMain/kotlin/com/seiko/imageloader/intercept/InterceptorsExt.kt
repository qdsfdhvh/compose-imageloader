package com.seiko.imageloader.intercept

import androidx.compose.runtime.identityHashCode
import androidx.compose.ui.graphics.painter.Painter
import com.seiko.imageloader.Bitmap
import com.seiko.imageloader.Image
import com.seiko.imageloader.cache.memory.MemoryCache
import com.seiko.imageloader.cache.memory.MemoryCacheBuilder
import com.seiko.imageloader.cache.memory.MemoryKey
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.size

fun InterceptorsBuilder.addInterceptor(block: suspend (chain: Interceptor.Chain) -> ImageResult) {
    addInterceptor(Interceptor(block))
}

fun InterceptorsBuilder.bitmapMemoryCache(
    mapToMemoryValue: (ImageResult) -> Bitmap? = { (it as? ImageResult.OfBitmap)?.bitmap },
    mapToImageResult: (Bitmap) -> ImageResult? = { ImageResult.OfBitmap(it) },
    memoryCache: () -> MemoryCache<MemoryKey, Bitmap>,
) {
    addDefaultMemoryCacheInterceptor(
        memoryInterceptor(
            mapToMemoryValue = mapToMemoryValue,
            mapToImageResult = mapToImageResult,
            memoryCache = memoryCache,
        ),
    )
}

fun InterceptorsBuilder.bitmapMemoryCacheConfig(
    valueHashProvider: (Bitmap) -> Int = { identityHashCode(it) },
    valueSizeProvider: (Bitmap) -> Int = { it.size },
    block: MemoryCacheBuilder<MemoryKey, Bitmap>.() -> Unit,
) {
    addDefaultMemoryCacheInterceptor(
        bitmapMemoryInterceptor(
            valueHashProvider = valueHashProvider,
            valueSizeProvider = valueSizeProvider,
            block = block,
        ),
    )
}

fun InterceptorsBuilder.imageMemoryCache(
    mapToMemoryValue: (ImageResult) -> Image? = { (it as? ImageResult.OfImage)?.image },
    mapToImageResult: (Image) -> ImageResult? = { ImageResult.OfImage(it) },
    memoryCache: () -> MemoryCache<MemoryKey, Image>,
) {
    addDefaultMemoryCacheInterceptor(
        memoryInterceptor(
            memoryCache = memoryCache,
            mapToMemoryValue = mapToMemoryValue,
            mapToImageResult = mapToImageResult,
        ),
    )
}

fun InterceptorsBuilder.imageMemoryCacheConfig(
    valueHashProvider: (Image) -> Int = { identityHashCode(it) },
    valueSizeProvider: (Image) -> Int = { 1 },
    block: MemoryCacheBuilder<MemoryKey, Image>.() -> Unit,
) {
    addDefaultMemoryCacheInterceptor(
        imageMemoryInterceptor(
            valueHashProvider = valueHashProvider,
            valueSizeProvider = valueSizeProvider,
            block = block,
        ),
    )
}

fun InterceptorsBuilder.painterMemoryCache(
    mapToMemoryValue: (ImageResult) -> Painter? = { (it as? ImageResult.OfPainter)?.painter },
    mapToImageResult: (Painter) -> ImageResult? = { ImageResult.OfPainter(it) },
    memoryCache: () -> MemoryCache<MemoryKey, Painter>,
) {
    addDefaultMemoryCacheInterceptor(
        memoryInterceptor(
            memoryCache = memoryCache,
            mapToMemoryValue = mapToMemoryValue,
            mapToImageResult = mapToImageResult,
        ),
    )
}

fun InterceptorsBuilder.painterMemoryCacheConfig(
    valueHashProvider: (Painter) -> Int = { identityHashCode(it) },
    valueSizeProvider: (Painter) -> Int = { 1 },
    block: MemoryCacheBuilder<MemoryKey, Painter>.() -> Unit,
) {
    addDefaultMemoryCacheInterceptor(
        painterMemoryInterceptor(
            valueHashProvider = valueHashProvider,
            valueSizeProvider = valueSizeProvider,
            block = block,
        ),
    )
}
