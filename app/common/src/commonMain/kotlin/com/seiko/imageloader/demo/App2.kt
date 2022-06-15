package com.seiko.imageloader.demo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.rememberAsyncImagePainter

@Composable
fun App2() {
    ComposeImageLoaderTheme {
        Scaffold { innerPadding ->
            Box(Modifier.padding(innerPadding)) {
                val resLoader = LocalResLoader.current
                val images = remember(resLoader) {
                    imageJsonData.decodeJson<List<Image>>().take(20)
                }

                val rowSize = 3
                val spacing = 0.dp

                val rows = remember {
                    images.windowed(rowSize, rowSize, true)
                }

                Column {
                    rows.forEach { row ->
                        Row(Modifier.fillMaxWidth()) {
                            val subSize = rowSize - row.size
                            for (i in row.indices) {
                                val image = row[i]
                                // val realIndex = i + index * rowSize
                                Box(modifier = Modifier.weight(1f)) {
                                    val painter = rememberAsyncImagePainter(
                                        url = image.url
                                    )
                                    Image(
                                        painter = painter,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(200.dp),
                                    )
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
                    }
                }


            }

        }
    }
}