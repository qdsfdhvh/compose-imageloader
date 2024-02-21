package com.seiko.imageloader.demo

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.demo.util.commonConfig
import com.seiko.imageloader.intercept.bitmapMemoryCacheConfig
import okio.Path.Companion.toPath
import platform.AppKit.NSApp
import platform.AppKit.NSApplication
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

fun main() {
    NSApplication.sharedApplication()
    Window("ComposeImageLoader") {
        CompositionLocalProvider(
            LocalImageLoader provides remember { generateImageLoader() },
        ) {
            App()
        }
    }
    NSApp?.run()
}

private fun generateImageLoader(): ImageLoader {
    return ImageLoader {
        commonConfig()
        components {
            setupDefaultComponents()
        }
        interceptor {
            bitmapMemoryCacheConfig {
                maxSize(32 * 1024 * 1024) // 32MB
            }
            diskCacheConfig {
                directory(getCacheDir().toPath().resolve("image_cache"))
                maxSizeBytes(512L * 1024 * 1024) // 512MB
            }
        }
    }
}

private fun getCacheDir(): String {
    return NSSearchPathForDirectoriesInDomains(
        NSCachesDirectory,
        NSUserDomainMask,
        true,
    ).first() as String
}
