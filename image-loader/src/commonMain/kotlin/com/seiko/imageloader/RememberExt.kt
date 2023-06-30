package com.seiko.imageloader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import com.seiko.imageloader.model.ImageAction
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.option.Scale

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

@Composable
fun rememberImagePainter(
    url: String,
    imageLoader: ImageLoader = LocalImageLoader.current,
    contentScale: ContentScale = ContentScale.Fit,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
    placeholderPainter: (@Composable () -> Painter)? = null,
    errorPainter: (@Composable () -> Painter)? = null,
): Painter {
    val action by rememberImageAction(
        request = remember(url, contentScale) {
            ImageRequest {
                data(url)
                scale(contentScale.toScale())
            }
        },
        imageLoader = imageLoader,
    )
    return rememberImageActionPainter(
        action = action,
        filterQuality = filterQuality,
        placeholderPainter = placeholderPainter,
        errorPainter = errorPainter,
    )
}

@Composable
fun rememberImagePainter(
    resId: Int,
    imageLoader: ImageLoader = LocalImageLoader.current,
    contentScale: ContentScale = ContentScale.Fit,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
    placeholderPainter: (@Composable () -> Painter)? = null,
    errorPainter: (@Composable () -> Painter)? = null,
): Painter {
    val action by rememberImageAction(
        request = remember(resId, contentScale) {
            ImageRequest {
                data(resId)
                scale(contentScale.toScale())
            }
        },
        imageLoader = imageLoader,
    )
    return rememberImageActionPainter(
        action = action,
        filterQuality = filterQuality,
        placeholderPainter = placeholderPainter,
        errorPainter = errorPainter,
    )
}

@Composable
fun rememberImagePainter(
    request: ImageRequest,
    imageLoader: ImageLoader = LocalImageLoader.current,
    contentScale: ContentScale = ContentScale.Fit,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
): Painter {
    val newRequest = remember(request) {
        request.newBuilder {
            scale(contentScale.toScale())
        }
    }
    return rememberImageActionPainter(newRequest, imageLoader, filterQuality)
}

private fun ContentScale.toScale() = when (this) {
    ContentScale.Fit, ContentScale.Inside -> Scale.FIT
    else -> Scale.FILL
}
