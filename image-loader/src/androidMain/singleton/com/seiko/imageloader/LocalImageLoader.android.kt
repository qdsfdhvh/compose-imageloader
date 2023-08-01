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

@Suppress("FunctionName")
fun ImageLoader.Companion.DefaultAndroid(context: Context): ImageLoader {
    return ImageLoader {
        options {
            androidContext(context.applicationContext)
        }
        components {
            setupDefaultComponents()
        }
        interceptor {
            defaultImageResultMemoryCache()
            memoryCacheConfig {
                maxSizePercent(context, 0.25)
            }
            diskCacheConfig {
                directory(context.cacheDir.resolve("image_cache").toOkioPath())
                maxSizeBytes(512L * 1024 * 1024) // 512MB
            }
        }
    }
}
