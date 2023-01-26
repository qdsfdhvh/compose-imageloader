package com.seiko.imageloader.demo.scene

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BigImagesScene(
    onBack: () -> Unit,
) {
    BackScene(
        onBack = onBack,
        title = { Text("Big") },
    ) { innerPadding ->
        Column(
            Modifier.padding(innerPadding).fillMaxSize()
        ) {
            ImageItem(OVER_SIZE_IMAGE)
        }
    }
}

private const val OVER_SIZE_IMAGE = "https://ipfs.io/ipfs/bafybeigk7niz7mzg4lykydiqhq5i7clmy7ybr6y5omn4uqh7ptihlrdtli"
