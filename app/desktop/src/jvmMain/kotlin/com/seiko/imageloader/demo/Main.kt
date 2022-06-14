package com.seiko.imageloader.demo

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.ImageLoaderBuilder
import com.seiko.imageloader.LocalImageLoader
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

fun main() {
    Napier.base(DebugAntilog())

    application {
        Window(
            title = "PreCompose Sample",
            onCloseRequest = {
                exitApplication()
            },
        ) {
            CompositionLocalProvider(
                LocalImageLoader provides generateImageLoader(),
            ) {
                App()
            }
        }
    }
}

private fun generateImageLoader(): ImageLoader {
    return ImageLoaderBuilder()
        .build()
}