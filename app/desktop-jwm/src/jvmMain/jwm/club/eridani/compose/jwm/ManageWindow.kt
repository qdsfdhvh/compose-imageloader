package club.eridani.compose.jwm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import io.github.humbleui.jwm.App

@Composable
fun ManageWindow(
    state: WindowState = WindowState(),
    visible: Boolean = true,
    title: String = "Untitled",
) {
    val window = LocalApplicationWindow.current
    val internalWindow = remember(window) { window.window }

    LaunchedEffect(visible) {
        internalWindow.setVisible(visible)
    }

    LaunchedEffect(title) {
        internalWindow.setTitle(title)
    }

    LaunchedEffect(state.isMinimized) {
        if (state.isMinimized) {
            internalWindow.minimize()
        } else {
            internalWindow.restore()
        }
    }

    LaunchedEffect(state.placement) {
        when (state.placement) {
            WindowPlacement.Floating -> internalWindow.restore()
            WindowPlacement.Maximized -> internalWindow.maximize()
            // TODO: JWM doesn't support fullscreen yet
            WindowPlacement.Fullscreen -> internalWindow.maximize()
        }
    }

    LaunchedEffect(state.size) {
        internalWindow.setWindowSize(state.size.width.value.toInt(), state.size.height.value.toInt())
    }

    LaunchedEffect(state.position) {
        internalWindow.setWindowPosition(state.position.x.value.toInt(), state.position.y.value.toInt())
    }
}
