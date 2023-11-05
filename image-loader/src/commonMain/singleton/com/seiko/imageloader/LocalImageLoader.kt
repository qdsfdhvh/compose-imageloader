package com.seiko.imageloader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.ReadOnlyComposable
import com.seiko.imageloader.cache.memory.MemoryCache
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.intercept.InterceptorsBuilder
import com.seiko.imageloader.model.ImageResult

val LocalImageLoader = createImageLoaderProvidableCompositionLocal()

// maybe it's no longer needed here
expect class ImageLoaderProvidableCompositionLocal {
    val current: ImageLoader
        @Composable
        @ReadOnlyComposable
        get

    infix fun provides(value: ImageLoader): ProvidedValue<*>
}

expect fun createImageLoaderProvidableCompositionLocal(): ImageLoaderProvidableCompositionLocal

fun ImageLoader.Companion.createDefault(): ImageLoader {
    return ImageLoader {
        components {
            setupDefaultComponents()
        }
        interceptor {
            defaultImageResultMemoryCache()
            memoryCacheConfig {
                maxSizeBytes(32 * 1024 * 1024) // 32MB
            }
        }
    }
}

// cache 100 image result, without bitmap
fun InterceptorsBuilder.defaultImageResultMemoryCache(
    includeBitmap: Boolean = false,
    saveSize: Int = 100,
    valueHashProvider: (ImageResult) -> Int = { it.hashCode() },
    valueSizeProvider: (ImageResult) -> Int = { 1 },
    mapToMemoryValue: (ImageResult) -> ImageResult? = {
        when (it) {
            is ImageResult.OfImage,
            is ImageResult.OfPainter,
            -> it
            is ImageResult.OfBitmap -> if (includeBitmap) it else null
            is ImageResult.OfSource,
            is ImageResult.OfError,
            -> null
        }
    },
    mapToImageResult: (ImageResult) -> ImageResult? = { it },
) {
    anyMemoryCache(
        mapToMemoryValue = mapToMemoryValue,
        mapToImageResult = mapToImageResult,
    ) {
        MemoryCache(
            valueHashProvider = valueHashProvider,
            valueSizeProvider = valueSizeProvider,
            block = {
                maxSizeBytes(saveSize)
            },
        )
    }
}
