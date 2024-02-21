package com.seiko.imageloader

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import com.seiko.imageloader.cache.memory.maxSizePercent
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.intercept.bitmapMemoryCacheConfig
import com.seiko.imageloader.intercept.imageMemoryCacheConfig
import com.seiko.imageloader.intercept.painterMemoryCacheConfig
import com.seiko.imageloader.option.androidContext
import okio.Path.Companion.toOkioPath

actual class ImageLoaderProvidableCompositionLocal constructor(
    val delegate: ProvidableCompositionLocal<ImageLoader?>,
) {
    actual inline val current: ImageLoader
        @Composable
        @ReadOnlyComposable
        get() = delegate.current ?: LocalContext.current.imageLoader

    actual infix fun provides(value: ImageLoader): ProvidedValue<*> {
        return delegate provides value
    }
}

actual fun createImageLoaderProvidableCompositionLocal() = ImageLoaderProvidableCompositionLocal(
    delegate = staticCompositionLocalOf { null },
)

fun ImageLoader.Companion.createDefaultAndroid(context: Context): ImageLoader {
    return ImageLoader {
        options {
            androidContext(context.applicationContext)
        }
        components {
            setupDefaultComponents()
        }
        interceptor {
            // cache 25% memory bitmap
            bitmapMemoryCacheConfig {
                maxSizePercent(context, 0.25)
            }
            // cache 50 image
            imageMemoryCacheConfig {
                maxSize(50)
            }
            // cache 50 painter
            painterMemoryCacheConfig {
                maxSize(50)
            }
            diskCacheConfig {
                directory(context.cacheDir.resolve("image_cache").toOkioPath())
                maxSizeBytes(512L * 1024 * 1024) // 512MB
            }
        }
    }
}
