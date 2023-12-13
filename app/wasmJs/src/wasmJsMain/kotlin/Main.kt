import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.CanvasBasedWindow
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.rememberImagePainter

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow {
        CompositionLocalProvider(
            LocalImageLoader provides remember { generateImageLoader() },
        ) {
            Box(Modifier.fillMaxSize()) {
                val painter = rememberImagePainter("https://images.unsplash.com/photo-1550973886-796d048c599f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjU4MjM5fQ")
                Image(
                    painter,
                    contentDescription = null,
                    modifier = Modifier.size(300.dp),
                )
            }
        }
    }
}

private fun generateImageLoader(): ImageLoader {
    return ImageLoader {
        components {
            setupDefaultComponents()
        }
        interceptor {
            memoryCacheConfig {
                maxSizeBytes(32 * 1024 * 1024) // 32MB
            }
        }
    }
}
