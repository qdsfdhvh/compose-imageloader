package com.seiko.imageloader.demo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.seiko.imageloader.rememberAsyncImagePainter
import com.seiko.imageloader.request.ImageRequestBuilder

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
                val request = remember {
                    ImageRequestBuilder()
                        .data("https://images.unsplash.com/photo-1550947176-68e708cb2dac?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjU4MjM5fQ")
                        .build()
                }
                Image(
                    rememberAsyncImagePainter(request),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}