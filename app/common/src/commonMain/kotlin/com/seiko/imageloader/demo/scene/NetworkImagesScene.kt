package com.seiko.imageloader.demo.scene

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.seiko.imageloader.demo.data.imageJsonData

@Composable
fun NetworkImagesScene(
    onBack: () -> Unit,
) {
    var showBlur by rememberSaveable { mutableStateOf(false) }
    BackScene(
        onBack = onBack,
        title = { Text("Network") },
        floatingActionButton = {
            FloatingActionButton({ showBlur = !showBlur }) {
                Icon(
                    if (showBlur) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                    contentDescription = "show blur",
                )
            }
        },
    ) { innerPadding ->
        val images = rememberImageList(imageJsonData)
        LazyVerticalGrid(
            GridCells.Fixed(3),
            Modifier.padding(innerPadding).fillMaxSize(),
        ) {
            items(images) { image ->
                ImageItem(
                    data = image.imageUrl,
                    blurRadius = if (showBlur) 15 else 0,
                )
            }
        }
    }
}
