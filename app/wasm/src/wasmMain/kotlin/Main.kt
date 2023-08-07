import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.seiko.imageloader.demo.App

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow("ComposeImageLoader") {
        App()
    }
}
