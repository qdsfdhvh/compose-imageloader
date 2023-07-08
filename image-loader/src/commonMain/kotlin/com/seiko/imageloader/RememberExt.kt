package com.seiko.imageloader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import com.seiko.imageloader.model.ImageAction
import com.seiko.imageloader.model.ImageRequest

@Composable
fun rememberImageAction(
    url: String,
    imageLoader: ImageLoader = LocalImageLoader.current,
): State<ImageAction> {
    val request = remember { ImageRequest(url) }
    return rememberImageAction(request, imageLoader)
}

@Composable
fun rememberImageAction(
    resId: Int,
    imageLoader: ImageLoader = LocalImageLoader.current,
): State<ImageAction> {
    val request = remember { ImageRequest(resId) }
    return rememberImageAction(request, imageLoader)
}

@Composable
fun rememberImagePainter(
    url: String,
    imageLoader: ImageLoader = LocalImageLoader.current,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
    placeholderPainter: (@Composable () -> Painter)? = null,
    errorPainter: (@Composable () -> Painter)? = null,
): Painter {
    val request = remember { ImageRequest(url) }
    return rememberImagePainter(
        request = request,
        imageLoader = imageLoader,
        filterQuality = filterQuality,
        placeholderPainter = placeholderPainter,
        errorPainter = errorPainter,
    )
}

@Composable
fun rememberImagePainter(
    resId: Int,
    imageLoader: ImageLoader = LocalImageLoader.current,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
    placeholderPainter: (@Composable () -> Painter)? = null,
    errorPainter: (@Composable () -> Painter)? = null,
): Painter {
    val request = remember { ImageRequest(resId) }
    return rememberImagePainter(
        request = request,
        imageLoader = imageLoader,
        filterQuality = filterQuality,
        placeholderPainter = placeholderPainter,
        errorPainter = errorPainter,
    )
}

@Composable
fun rememberImagePainter(
    request: ImageRequest,
    imageLoader: ImageLoader = LocalImageLoader.current,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
    placeholderPainter: (@Composable () -> Painter)? = request.placeholderPainter,
    errorPainter: (@Composable () -> Painter)? = request.errorPainter,
): Painter {
    val action by rememberImageAction(request, imageLoader)
    return rememberImageActionPainter(
        action = action,
        filterQuality = filterQuality,
        placeholderPainter = placeholderPainter,
        errorPainter = errorPainter,
    )
}
