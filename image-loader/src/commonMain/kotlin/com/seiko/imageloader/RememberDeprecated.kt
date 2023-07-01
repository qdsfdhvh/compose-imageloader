package com.seiko.imageloader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.option.Scale

@Deprecated("rememberImagePainter is deprecated", replaceWith = ReplaceWith("rememberImageActionPainter"))
@Composable
fun rememberImagePainter(
    url: String,
    imageLoader: ImageLoader = LocalImageLoader.current,
    contentScale: ContentScale = ContentScale.Fit,
    filterQuality: FilterQuality = DefaultFilterQuality,
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

@Deprecated("rememberImagePainter is deprecated", replaceWith = ReplaceWith("rememberImageActionPainter"))
@Composable
fun rememberImagePainter(
    resId: Int,
    imageLoader: ImageLoader = LocalImageLoader.current,
    contentScale: ContentScale = ContentScale.Fit,
    filterQuality: FilterQuality = DefaultFilterQuality,
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

@Deprecated("rememberImagePainter is deprecated", replaceWith = ReplaceWith("rememberImageActionPainter"))
@Composable
fun rememberImagePainter(
    request: ImageRequest,
    imageLoader: ImageLoader = LocalImageLoader.current,
    contentScale: ContentScale = ContentScale.Fit,
    filterQuality: FilterQuality = DefaultFilterQuality,
): Painter {
    val newRequest = remember(request) {
        request.newBuilder {
            scale(contentScale.toScale())
        }
    }
    return rememberImageActionPainter(newRequest, imageLoader, filterQuality)
}

@Deprecated("rememberAsyncImagePainter is deprecated", replaceWith = ReplaceWith("rememberImageActionPainter"))
@Composable
fun rememberAsyncImagePainter(
    url: String,
    imageLoader: ImageLoader = LocalImageLoader.current,
    contentScale: ContentScale = ContentScale.Fit,
    filterQuality: FilterQuality = DefaultFilterQuality,
): Painter {
    val request = remember(url) {
        ImageRequest {
            data(url)
            scale(contentScale.toScale())
        }
    }
    return rememberImageActionPainter(
        request = request,
        imageLoader = imageLoader,
        filterQuality = filterQuality,
    )
}

@Deprecated("rememberAsyncImagePainter is deprecated", replaceWith = ReplaceWith("rememberImageActionPainter"))
@Composable
fun rememberAsyncImagePainter(
    resId: Int,
    imageLoader: ImageLoader = LocalImageLoader.current,
    contentScale: ContentScale = ContentScale.Fit,
    filterQuality: FilterQuality = DefaultFilterQuality,
): Painter {
    val request = remember(resId) {
        ImageRequest {
            data(resId)
            scale(contentScale.toScale())
        }
    }
    return rememberImageActionPainter(
        request = request,
        imageLoader = imageLoader,
        filterQuality = filterQuality,
    )
}

@Deprecated("rememberAsyncImagePainter is deprecated", replaceWith = ReplaceWith("rememberImageActionPainter"))
@Composable
fun rememberAsyncImagePainter(
    request: ImageRequest,
    imageLoader: ImageLoader = LocalImageLoader.current,
    contentScale: ContentScale = ContentScale.Fit,
    filterQuality: FilterQuality = DefaultFilterQuality,
): Painter {
    val newRequest = remember(request) {
        request.newBuilder {
            scale(contentScale.toScale())
        }
    }
    return rememberImageActionPainter(
        request = newRequest,
        imageLoader = imageLoader,
        filterQuality = filterQuality,
    )
}

fun ContentScale.toScale() = when (this) {
    ContentScale.Fit, ContentScale.Inside -> Scale.FIT
    else -> Scale.FILL
}
