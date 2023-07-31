package com.seiko.imageloader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.ReadOnlyComposable
import com.seiko.imageloader.component.setupDefaultComponents
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
            // cache 100 image result, without bitmap
            anyMemoryCacheConfig(
                valueHashProvider = { it.hashCode() },
                valueSizeProvider = { 1 },
                mapToMemoryValue = { imageResult ->
                    if (imageResult is ImageResult.Bitmap) {
                        null
                    } else {
                        imageResult
                    }
                },
                mapToImageResult = { it },
            ) {
                maxSizeBytes(100)
            }
            // here cache bitmap
            memoryCacheConfig {
                maxSizeBytes(32 * 1024 * 1024) // 32MB
            }
        }
    }
