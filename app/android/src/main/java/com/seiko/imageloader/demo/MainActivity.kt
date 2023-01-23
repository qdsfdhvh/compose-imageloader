package com.seiko.imageloader.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.cache.memory.maxSizePercent
import com.seiko.imageloader.component.setupDefaultComponents
import okio.Path.Companion.toOkioPath

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompositionLocalProvider(
                LocalImageLoader provides generateImageLoader(),
                LocalResLoader provides ResLoader { this },
            ) {
                App()
            }
        }
    }

    private fun generateImageLoader(): ImageLoader {
        return ImageLoader {
            commonConfig()
            components {
                setupDefaultComponents(applicationContext)
            }
            interceptor {
                memoryCache {
                    // Set the max size to 25% of the app's available memory.
                    maxSizePercent(applicationContext, 0.25)
                }
                diskCache {
                    directory(cacheDir.resolve("image_cache").toOkioPath())
                    maxSizeBytes(512L * 1024 * 1024) // 512MB
                }
            }
        }
    }
}
