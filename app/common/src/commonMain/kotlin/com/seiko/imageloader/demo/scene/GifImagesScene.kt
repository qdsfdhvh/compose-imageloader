package com.seiko.imageloader.demo.scene

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.seiko.imageloader.demo.data.imageJsonDataGif
import com.seiko.imageloader.demo.icon.rememberPauseCircle
import com.seiko.imageloader.demo.icon.rememberPlayCircle

@Composable
fun GifImagesScene(
    onBack: () -> Unit,
) {
    var playAnime by rememberSaveable { mutableStateOf(true) }
    BackScene(
        onBack = onBack,
        title = { Text("Gif") },
        floatingActionButton = {
            FloatingActionButton({ playAnime = !playAnime }) {
                Icon(
                    if (playAnime) rememberPauseCircle() else rememberPlayCircle(),
                    contentDescription = "play anime",
                )
            }
        },
    ) { innerPadding ->
        val images by rememberImageList(imageJsonDataGif)
        LazyVerticalGrid(
            GridCells.Fixed(3),
            Modifier.padding(innerPadding).fillMaxSize(),
        ) {
            items(images) { image ->
                ImageItem(
                    data = image.imageUrl,
                    playAnime = playAnime,
                )
            }
        }
    }
}
