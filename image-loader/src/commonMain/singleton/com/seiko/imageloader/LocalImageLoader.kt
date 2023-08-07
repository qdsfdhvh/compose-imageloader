package com.seiko.imageloader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.ReadOnlyComposable
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

val ImageLoader.Companion.Default: ImageLoader
    get() = ImageLoader {
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

// cache 100 image result, without bitmap
fun InterceptorsBuilder.defaultImageResultMemoryCache(
    includeBitmap: Boolean = false,
    saveSize: Int = 100,
    valueHashProvider: (ImageResult) -> Int = { it.hashCode() },
    valueSizeProvider: (ImageResult) -> Int = { 1 },
    mapToMemoryValue: (ImageResult) -> ImageResult? = {
        when (it) {
            is ImageResult.Image,
            is ImageResult.Painter,
            -> it
            is ImageResult.Bitmap -> if (includeBitmap) it else null
            is ImageResult.Source,
            is ImageResult.Error,
            -> null
        }
    },
    mapToImageResult: (ImageResult) -> ImageResult? = { it },
) {
    anyMemoryCacheConfig(
        valueHashProvider = valueHashProvider,
        valueSizeProvider = valueSizeProvider,
        mapToMemoryValue = mapToMemoryValue,
        mapToImageResult = mapToImageResult,
    ) {
        maxSizeBytes(saveSize)
    }
}
