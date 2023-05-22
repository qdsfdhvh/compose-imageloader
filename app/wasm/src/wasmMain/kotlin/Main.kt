import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.CanvasBasedWindow
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow("ComposeImageLoader") {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.Cyan.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center,
        ) {
            Text("App")
        }
    }
}