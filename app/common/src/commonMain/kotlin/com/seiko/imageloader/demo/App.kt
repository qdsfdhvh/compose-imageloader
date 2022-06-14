package com.seiko.imageloader.demo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.rememberAsyncImagePainter

@Composable
fun App() {
    ComposeImageLoaderTheme {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = { }) {
                    Icon(Icons.Filled.Add, null)
                }
            },
            topBar = {
                TopAppBar(
                    title = {
                        Text("Material 3")
                    }
                )
            }
        ) { innerPadding ->
            Box(Modifier.padding(innerPadding)) {
                ImageList(Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
private fun ImageList(modifier: Modifier = Modifier) {
    val resLoader = LocalResLoader.current
    val images = remember(resLoader) {
        resLoader.getString(MR.assets.jpgs).decodeJson<List<Image>>()
    }
    LazyVerticalGrid(columns = GridCells.Adaptive(200.dp), modifier = modifier) {
        items(images) { image ->
            Image(
                rememberAsyncImagePainter(image.url),
                contentDescription = null,
                modifier = Modifier.aspectRatio(1f),
            )
        }
    }
}
