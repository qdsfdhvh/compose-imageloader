import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.Window
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.ImageLoaderBuilder
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.cache.memory.MemoryCacheBuilder
import com.seiko.imageloader.demo.App
import com.seiko.imageloader.demo.LocalResLoader
import com.seiko.imageloader.demo.ResLoader
import kotlinx.browser.window
import kotlinx.coroutines.launch
import org.jetbrains.skiko.wasm.onWasmReady
import org.w3c.dom.events.WheelEvent
import androidx.compose.foundation.gestures.scrollBy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

fun main() {
    val scope = CoroutineScope(Dispatchers.Main)
    val lazyListScope = LazyListState()
    window.addEventListener("wheel", { event ->
        if (event is WheelEvent) {
            event.stopPropagation()
            scope.launch {
                lazyListScope.scrollBy(event.deltaY.toFloat())
            }
        }
    })
    onWasmReady {
        Window("ComposeImageLoader") {
            CompositionLocalProvider(
                LocalImageLoader provides generateImageLoader(),
                LocalResLoader provides ResLoader(),
            ) {
                App(
                    lazyListScope = lazyListScope,
                )
            }
        }
    }
}

private fun generateImageLoader(): ImageLoader {
    return ImageLoaderBuilder()
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
