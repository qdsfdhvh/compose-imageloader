package club.eridani.compose.jwm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.WindowInfo

class JWMWindowInfo : WindowInfo {
    override var isWindowFocused: Boolean by mutableStateOf(true)
}