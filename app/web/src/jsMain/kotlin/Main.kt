import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.demo.App
import com.seiko.imageloader.demo.util.commonConfig
import okio.FileSystem
import okio.fakefilesystem.FakeFileSystem
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.configureWebResources
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class, ExperimentalResourceApi::class)
fun main() {
    configureWebResources {
        // Not necessary - It's the same as the default. We add it here just to present this feature.
        resourcePathMapping { path -> "./$path" }
    }
    onWasmReady {
        CanvasBasedWindow("ComposeImageLoader") {
            CompositionLocalProvider(
                LocalImageLoader provides remember { generateImageLoader() },
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
            setupDefaultComponents()
        }
        interceptor {
            memoryCacheConfig {
                maxSizeBytes(32 * 1024 * 1024) // 32MB
            }
            diskCacheConfig(FakeFileSystem().apply { emulateUnix() }) {
                directory(FileSystem.SYSTEM_TEMPORARY_DIRECTORY)
                maxSizeBytes(256L * 1024 * 1024) // 256MB
            }
        }
    }
}
