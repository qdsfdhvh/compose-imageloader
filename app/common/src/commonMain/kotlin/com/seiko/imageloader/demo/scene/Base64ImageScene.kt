package com.seiko.imageloader.demo.scene

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun Base64ImageScene(
    onBack: () -> Unit,
) {
    BackScene(
        onBack = onBack,
        title = { Text("Base64") },
    ) {
    }
}
