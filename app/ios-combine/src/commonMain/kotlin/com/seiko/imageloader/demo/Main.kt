package com.seiko.imageloader.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeUIViewController
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.cache.memory.maxSizePercent
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.demo.util.LocalResLoader
import com.seiko.imageloader.demo.util.ResLoader
import com.seiko.imageloader.demo.util.commonConfig
import okio.Path.Companion.toPath
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import platform.UIKit.UILayoutGuide
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.UIKit.accessibilityFrame
import platform.UIKit.safeAreaLayoutGuide

@Suppress("FunctionName")
fun MainViewController(window: UIWindow): UIViewController = ComposeUIViewController {
    CompositionLocalProvider(
        LocalImageLoader provides generateImageLoader(),
        LocalResLoader provides ResLoader(),
    ) {
        Column {
            App(
                modifier = Modifier
                    .windowInsetsPadding(window.safeAreaLayoutGuide.toWindowInsets()),
            )
        }
    }
}

private fun UILayoutGuide.toWindowInsets() = WindowInsets(
    top = topAnchor.accessibilityFrame.size.dp,
    // bottom = bottomAnchor.accessibilityFrame.size.dp,
    // left = leftAnchor.accessibilityFrame.size.dp,
    // right = rightAnchor.accessibilityFrame.size.dp,
)

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
        NSCachesDirectory,
        NSUserDomainMask,
        null,
        true,
        null,
    )!!.path.orEmpty()
}
