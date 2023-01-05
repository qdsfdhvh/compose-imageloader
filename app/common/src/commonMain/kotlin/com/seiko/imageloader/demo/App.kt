package com.seiko.imageloader.demo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.seiko.imageloader.ImageRequestState
import com.seiko.imageloader.rememberAsyncImagePainter
import com.seiko.imageloader.request.ImageRequestBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

private const val overSize = "https://ipfs.io/ipfs/bafybeigk7niz7mzg4lykydiqhq5i7clmy7ybr6y5omn4uqh7ptihlrdtli"

@Composable
private fun ImageList(modifier: Modifier = Modifier) {
    var images by remember { mutableStateOf(emptyList<Image>()) }
    LaunchedEffect(Unit) {
        images = withContext(Dispatchers.Default) {
            val jpegs = imageJsonData.decodeJson<List<Image>>()
            val gifs = imageJsonDataGif.decodeJson<List<Image>>()
            val svgs = imageJsonDataSvg.decodeJson<List<Image>>()
            (jpegs + gifs + svgs).shuffled()
        }
    }

    LazyVerticalGrid(
        GridCells.Fixed(3),
        modifier = modifier,
    ) {
        item {
            ImageItem(overSize)
        }
        items(images) { image ->
            ImageItem(image.url)
        }
    }
}

@Composable
fun ImageItem(url: String) {
    Box(Modifier.aspectRatio(1f), Alignment.Center) {
        val request = remember {
            ImageRequestBuilder()
                .data(url)
                .addInterceptor(NullDataInterceptor)
                .components {
                    add(CustomKtorUrlFetcher.Factory())
                }
                .build()
        }
        val painter = rememberAsyncImagePainter(request)
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        when (val requestState = painter.requestState) {
            ImageRequestState.Loading -> {
                CircularProgressIndicator()
            }
            is ImageRequestState.Failure -> {
                Text(requestState.error.message ?: "Error")
            }
            ImageRequestState.Success -> Unit
        }
    }
}
