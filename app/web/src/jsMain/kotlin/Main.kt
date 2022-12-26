import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.Window
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.ImageLoaderBuilder
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.cache.memory.MemoryCacheBuilder
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
    return ImageLoaderBuilder()
        .commonConfig()
        .memoryCache {
            MemoryCacheBuilder()
                // Set the max size to 25% of the app's available memory.
                .maxSizePercent(0.25)
                .build()
        }
        // .diskCache {
        //     DiskCacheBuilder()
        //         .directory(cacheDir.resolve("image_cache").toOkioPath())
        //         .maxSizeBytes(512L * 1024 * 1024) // 512MB
        //         .build()
        // }
        .build()
}
