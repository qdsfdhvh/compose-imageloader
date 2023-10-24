package com.seiko.imageloader.demo

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.seiko.imageloader.DefaultApple
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.demo.util.commonConfig
import platform.UIKit.UIViewController

@Suppress("FunctionName")
fun MainViewController(): UIViewController = ComposeUIViewController {
    CompositionLocalProvider(
        LocalImageLoader provides remember { generateImageLoader() },
    ) {
        App()
    }
}

private fun generateImageLoader(): ImageLoader {
    return ImageLoader {
        takeFrom(ImageLoader.DefaultApple)
        commonConfig()
    }
    // return ImageLoader {
    //     commonConfig()
    //     components {
    //         setupDefaultComponents()
    //     }
    //     interceptor {
    //         memoryCacheConfig {
    //             maxSizeBytes(32 * 1024 * 1024) // 32MB
    //         }
    //         diskCacheConfig {
    //             directory(getCacheDir().toPath().resolve("image_cache"))
    //             maxSizeBytes(512L * 1024 * 1024) // 512MB
    //         }
    //     }
    // }
}

// private fun getCacheDir(): String {
//     return NSSearchPathForDirectoriesInDomains(
//         NSCachesDirectory,
//         NSUserDomainMask,
//         true,
//     ).first() as String
// }
