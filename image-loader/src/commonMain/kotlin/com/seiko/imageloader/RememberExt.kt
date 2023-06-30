package com.seiko.imageloader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import com.seiko.imageloader.model.ImageAction
import com.seiko.imageloader.model.ImageRequest

@Composable
inline fun rememberImageAction(
    url: String,
    imageLoader: ImageLoader = LocalImageLoader.current,
): State<ImageAction> {
    val request = remember { ImageRequest(url) }
    return rememberImageAction(request, imageLoader)
}

@Composable
inline fun rememberImageAction(
    resId: Int,
    imageLoader: ImageLoader = LocalImageLoader.current,
): State<ImageAction> {
    val request = remember { ImageRequest(resId) }
    return rememberImageAction(request, imageLoader)
}

@Composable
inline fun rememberImageActionPainter(
    url: String,
    imageLoader: ImageLoader = LocalImageLoader.current,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
): Painter {
    val request = remember { ImageRequest(url) }
    return rememberImageActionPainter(request, imageLoader, filterQuality)
}

@Composable
inline fun rememberImageActionPainter(
    resId: Int,
    imageLoader: ImageLoader = LocalImageLoader.current,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
): Painter {
    val request = remember { ImageRequest(resId) }
    return rememberImageActionPainter(request, imageLoader, filterQuality)
}
