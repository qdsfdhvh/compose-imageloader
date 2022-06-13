package com.seiko.imageloader.demo

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() {
    application {
        Window(
            title = "PreCompose Sample",
            onCloseRequest = {
                exitApplication()
            },
        ) {
            App()
        }
    }
}