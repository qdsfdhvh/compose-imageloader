package com.seiko.imageloader.demo

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.Window
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.ImageLoaderBuilder
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.cache.disk.DiskCacheBuilder
import com.seiko.imageloader.cache.memory.MemoryCacheBuilder
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
    return ImageLoaderBuilder()
        .commonConfig()
        .memoryCache {
            MemoryCacheBuilder()
                // Set the max size to 25% of the app's available memory.
                .maxSizePercent(0.25)
                .build()
        }
        .diskCache {
            DiskCacheBuilder()
                .directory(getCacheDir().toPath().resolve("image_cache"))
                .maxSizeBytes(512L * 1024 * 1024) // 512MB
                .build()
        }
        .build()
}

private fun getCacheDir(): String {
    return NSFileManager.defaultManager.URLForDirectory(
        NSCachesDirectory, NSUserDomainMask, null, true, null
    )!!.path.orEmpty()
}
