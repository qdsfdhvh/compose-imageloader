package com.seiko.imageloader.demo.scene

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import com.seiko.imageloader.demo.model.Image
import com.seiko.imageloader.demo.util.NullDataInterceptor
import com.seiko.imageloader.demo.util.decodeJson
import com.seiko.imageloader.model.ImageEvent
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.model.ImageRequestBuilder
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.rememberImageAction
import com.seiko.imageloader.rememberImageActionPainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
    backgroundColor: Color = MaterialTheme.colors.background,
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
        backgroundColor = backgroundColor,
        content = content,
    )
}

@Composable
fun ImageItem(
    data: Any,
    modifier: Modifier = Modifier.aspectRatio(1f),
    contentScale: ContentScale = ContentScale.Crop,
    block: (ImageRequestBuilder.() -> Unit)? = null,
) {
    Box(modifier, Alignment.Center) {
        val request = remember(data, block) {
            ImageRequest {
                data(data)
                addInterceptor(NullDataInterceptor)
                // components {
                //     add(customKtorUrlFetcher)
                // }
                options {
                    maxImageSize = 512
                }
                block?.invoke(this)
            }
        }
        val action by rememberImageAction(request)
        val painter = rememberImageActionPainter(action)
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = contentScale,
            modifier = Modifier.fillMaxSize(),
        )
        when (val current = action) {
            is ImageEvent.StartWithDisk,
            is ImageEvent.StartWithFetch,
            -> {
                CircularProgressIndicator()
            }
            is ImageResult.Source -> {
                Text("image result is source")
            }
            is ImageResult.Error -> {
                Text(current.error.message ?: "Error")
            }
            else -> Unit
        }
    }
}

@Composable
fun rememberImageList(content: String): State<List<Image>> {
    return produceState(emptyList()) {
        value = withContext(Dispatchers.Default) {
            content.decodeJson()
        }
    }
}
