package com.seiko.imageloader.demo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.ImageRequestState
import com.seiko.imageloader.rememberAsyncImagePainter
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

    LazyColumn(modifier = modifier) {
        itemsGridIndexed(images, rowSize = 3) { _, image ->
            Box(Modifier.aspectRatio(1f), Alignment.Center) {
                val painter = rememberAsyncImagePainter(image.imageUrl)
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
    }
}

fun <T> LazyListScope.itemsGridIndexed(
    data: List<T>,
    rowSize: Int,
    spacing: Dp = 0.dp,
    padding: Dp = 0.dp,
    itemContent: @Composable BoxScope.(Int, T) -> Unit,
) {
    val rows = data.windowed(rowSize, rowSize, true)
    itemsIndexed(rows) { index, row ->
        Column(
            modifier = Modifier
                .fillParentMaxWidth()
                .padding(horizontal = padding)
        ) {
            Row(Modifier.fillMaxWidth()) {
                val subSize = rowSize - row.size
                for (i in row.indices) {
                    val item = row[i]
                    val realIndex = i + index * rowSize
                    Box(modifier = Modifier.weight(1f)) {
                        itemContent(realIndex, item)
                    }
                    if (i != row.lastIndex && subSize == 0) {
                        Spacer(modifier = Modifier.width(spacing))
                    }
                }

                if (subSize > 0) {
                    for (j in 0 until subSize) {
                        Spacer(modifier = Modifier.width(spacing))
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
            if (index != rows.lastIndex) {
                Spacer(modifier = Modifier.height(spacing))
            }
        }
    }
}
