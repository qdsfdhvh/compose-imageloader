import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.Window
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.cache.memory.MemoryCacheBuilder
import com.seiko.imageloader.component.setupBase64Components
import com.seiko.imageloader.component.setupCommonComponents
import com.seiko.imageloader.component.setupKtorComponents
import com.seiko.imageloader.component.setupSkiaComponents
import com.seiko.imageloader.demo.App
import com.seiko.imageloader.demo.LocalResLoader
import com.seiko.imageloader.demo.ResLoader
import com.seiko.imageloader.demo.commonConfig
import org.jetbrains.skiko.wasm.onWasmReady

fun main() {
    onWasmReady {
        Window("ComposeImageLoader") {
            CompositionLocalProvider(
                LocalImageLoader provides generateImageLoader(),
                LocalResLoader provides ResLoader(),
            ) {
                App()
            }
        }
    }
}

private fun generateImageLoader(): ImageLoader {
    return ImageLoader {
        commonConfig()
        components {
            setupKtorComponents()
            setupBase64Components()
            setupCommonComponents()
            setupSkiaComponents(imageScope)
        }
        interceptor {
            memoryCache {
                MemoryCacheBuilder()
                    // Set the max size to 25% of the app's available memory.
                    .maxSizePercent(0.25)
                    .build()
            }
            // diskCache {
            //     DiskCacheBuilder()
            //         .directory(getCacheDir().toPath().resolve("image_cache"))
            //         .maxSizeBytes(512L * 1024 * 1024) // 512MB
            //         .build()
            // }
        }
    }
}
