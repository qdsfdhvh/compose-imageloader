package com.seiko.imageloader.demo.scene

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun FilePickerScene(onBack: () -> Unit) {
    BackScene(
        onBack = onBack,
        title = { Text("FilePicker") },
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding).fillMaxSize()) {
            Text("compose-multiplatform-file-picker no support for current target.")
        }
    }
}
