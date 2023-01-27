package com.seiko.imageloader.demo.scene

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.seiko.imageloader.ImageRequestState
import com.seiko.imageloader.demo.model.Image
import com.seiko.imageloader.demo.util.NullDataInterceptor
import com.seiko.imageloader.demo.util.customKtorUrlFetcher
import com.seiko.imageloader.demo.util.decodeJson
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.model.blur
import com.seiko.imageloader.rememberAsyncImagePainter

@Composable
fun BackButton(onBack: () -> Unit) {
    IconButton(onBack) {
        Icon(Icons.Default.ArrowBack, "back")
    }
}

@Composable
fun BackScene(
    onBack: () -> Unit,
    title: @Composable () -> Unit,
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    BackButton(onBack)
                },
                title = title,
            )
        },
        floatingActionButton = floatingActionButton,
        content = content,
    )
}

@Composable
fun ImageItem(
    url: String,
    blurRadius: Int = 0,
) {
    Box(Modifier.aspectRatio(1f), Alignment.Center) {
        val request = remember(url, blurRadius) {
            ImageRequest {
                data(url)
                addInterceptor(NullDataInterceptor)
                if (blurRadius > 0) {
                    blur(blurRadius)
                }
                components {
                    add(customKtorUrlFetcher)
                }
            }
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

@Composable
fun rememberImageList(content: String): List<Image> {
    val images: List<Image> by produceState(emptyList()) {
        value = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Default) {
            content.decodeJson()
        }
    }
    return images
}
