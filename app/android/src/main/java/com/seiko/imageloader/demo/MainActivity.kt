package com.seiko.imageloader.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.createDefaultAndroid
import com.seiko.imageloader.demo.util.commonConfig

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompositionLocalProvider(
                LocalImageLoader provides remember { generateImageLoader() },
            ) {
                App()
            }
        }
    }

    private fun generateImageLoader(): ImageLoader {
        return ImageLoader {
            takeFrom(ImageLoader.createDefaultAndroid(applicationContext))
            commonConfig()
        }
        //     return ImageLoader {
        //         commonConfig()
        //         options {
        //             androidContext(applicationContext)
        //         }
        //         components {
        //             setupDefaultComponents()
        //         }
        //         interceptor {
        //             // cache 25% memory bitmap
        //             bitmapMemoryCacheConfig {
        //                 maxSizePercent(context, 0.25)
        //             }
        //             // cache 50 image
        //             imageMemoryCacheConfig {
        //                 maxSize(50)
        //             }
        //             // cache 50 painter
        //             painterMemoryCacheConfig {
        //                 maxSize(50)
        //             }
        //             diskCacheConfig {
        //                 directory(cacheDir.resolve("image_cache").toOkioPath())
        //                 maxSizeBytes(512L * 1024 * 1024) // 512MB
        //             }
        //         }
        //     }
    }
}
