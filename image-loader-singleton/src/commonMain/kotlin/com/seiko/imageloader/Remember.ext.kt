package com.seiko.imageloader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.graphics.painter.Painter
import com.seiko.imageloader.model.ImageAction
import com.seiko.imageloader.model.ImageRequest

@Composable
fun rememberImageAction(
    request: ImageRequest,
): State<ImageAction> {
    return rememberImageAction(request, LocalImageLoader.current)
}

@Composable
fun rememberImageAction(
    url: String,
    imageLoader: ImageLoader = LocalImageLoader.current,
): State<ImageAction> {
    val request = remember(url) { ImageRequest(url) }
    return rememberImageAction(request, imageLoader)
}

@Composable
fun rememberImageAction(
    resId: Int,
    imageLoader: ImageLoader = LocalImageLoader.current,
): State<ImageAction> {
    val request = remember(resId) { ImageRequest(resId) }
    return rememberImageAction(request, imageLoader)
}

@Composable
fun rememberImagePainter(
    request: ImageRequest,
    filterQuality: FilterQuality = DefaultFilterQuality,
    placeholderPainter: (@Composable () -> Painter)? = null,
    errorPainter: (@Composable () -> Painter)? = null,
): Painter {
    return rememberImagePainter(
        request = request,
        imageLoader = LocalImageLoader.current,
        filterQuality = filterQuality,
        placeholderPainter = placeholderPainter,
        errorPainter = errorPainter,
    )
}

@Composable
fun rememberImagePainter(
    url: String,
    imageLoader: ImageLoader = LocalImageLoader.current,
    filterQuality: FilterQuality = DefaultFilterQuality,
    placeholderPainter: (@Composable () -> Painter)? = null,
    errorPainter: (@Composable () -> Painter)? = null,
): Painter {
    val request = remember(url) { ImageRequest(url) }
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
    filterQuality: FilterQuality = DefaultFilterQuality,
    placeholderPainter: (@Composable () -> Painter)? = null,
    errorPainter: (@Composable () -> Painter)? = null,
): Painter {
    val request = remember(resId) { ImageRequest(resId) }
    return rememberImagePainter(
        request = request,
        imageLoader = imageLoader,
        filterQuality = filterQuality,
        placeholderPainter = placeholderPainter,
        errorPainter = errorPainter,
    )
}
