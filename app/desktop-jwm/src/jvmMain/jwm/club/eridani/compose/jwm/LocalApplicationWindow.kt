package club.eridani.compose.jwm

import androidx.compose.runtime.compositionLocalOf

val LocalApplicationWindow = compositionLocalOf<ApplicationWindow> { error("No window found") }