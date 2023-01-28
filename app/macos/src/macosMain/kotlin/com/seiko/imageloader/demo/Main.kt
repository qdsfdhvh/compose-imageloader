package com.seiko.imageloader.demo

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.Window
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.cache.memory.maxSizePercent
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.demo.util.LocalResLoader
import com.seiko.imageloader.demo.util.ResLoader
import com.seiko.imageloader.demo.util.commonConfig
import okio.Path.Companion.toPath
import platform.AppKit.NSApp
import platform.AppKit.NSApplication
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

fun main() {
    NSApplication.sharedApplication()
    Window("ComposeImageLoader") {
        CompositionLocalProvider(
            LocalImageLoader provides generateImageLoader(),
            LocalResLoader provides ResLoader(),
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
            setupDefaultComponents(imageScope)
        }
        interceptor {
            memoryCacheConfig {
                // Set the max size to 25% of the app's available memory.
                maxSizePercent(0.25)
            }
            diskCacheConfig {
                directory(getCacheDir().toPath().resolve("image_cache"))
                maxSizeBytes(512L * 1024 * 1024) // 512MB
            }
        }
    }
}

private fun getCacheDir(): String {
    return NSFileManager.defaultManager.URLForDirectory(
        NSCachesDirectory, NSUserDomainMask, null, true, null
    )!!.path.orEmpty()
}
