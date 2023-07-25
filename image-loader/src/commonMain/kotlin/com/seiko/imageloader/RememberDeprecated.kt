package com.seiko.imageloader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.option.toScale

@Deprecated(
    message = "move contentScale into ImageRequest",
    replaceWith = ReplaceWith("ImageRequest { scale(contentScale.toScale()) }"),
)
@Composable
fun rememberImagePainter(
    url: String,
    contentScale: ContentScale,
    imageLoader: ImageLoader = LocalImageLoader.current,
    filterQuality: FilterQuality = DefaultFilterQuality,
    placeholderPainter: (@Composable () -> Painter)? = null,
    errorPainter: (@Composable () -> Painter)? = null,
): Painter {
    return rememberImagePainter(
        request = remember(url, contentScale) {
            ImageRequest {
                data(url)
                scale(contentScale.toScale())
            }
        },
        imageLoader = imageLoader,
        filterQuality = filterQuality,
    )
}

@Deprecated(
    message = "move contentScale into ImageRequest",
    replaceWith = ReplaceWith("ImageRequest { scale(contentScale.toScale()) }"),
)
@Composable
fun rememberImagePainter(
    resId: Int,
    contentScale: ContentScale,
    imageLoader: ImageLoader = LocalImageLoader.current,
    filterQuality: FilterQuality = DefaultFilterQuality,
    placeholderPainter: (@Composable () -> Painter)? = null,
    errorPainter: (@Composable () -> Painter)? = null,
): Painter {
    return rememberImagePainter(
        request = remember(resId, contentScale) {
            ImageRequest {
                data(resId)
                scale(contentScale.toScale())
            }
        },
        imageLoader = imageLoader,
        filterQuality = filterQuality,
    )
}

@Deprecated(
    message = "move contentScale into ImageRequest",
    replaceWith = ReplaceWith("ImageRequest { scale(contentScale.toScale()) }"),
)
@Composable
fun rememberImagePainter(
    request: ImageRequest,
    contentScale: ContentScale,
    imageLoader: ImageLoader = LocalImageLoader.current,
    filterQuality: FilterQuality = DefaultFilterQuality,
): Painter {
    return rememberImagePainter(
        request = remember(request) {
            request.newBuilder {
                scale(contentScale.toScale())
            }
        },
        imageLoader = imageLoader,
        filterQuality = filterQuality,
    )
}

@Deprecated("Use rememberImageAction&rememberImageActionPainter or rememberImagePainter")
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
    val action by rememberImageAction(request, imageLoader)
    return rememberImageActionPainter(action, filterQuality)
}

@Deprecated("Use rememberImageAction&rememberImageActionPainter or rememberImagePainter")
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
    val action by rememberImageAction(request, imageLoader)
    return rememberImageActionPainter(action, filterQuality)
}

@Deprecated("Use rememberImageAction&rememberImageActionPainter or rememberImagePainter")
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
    val action by rememberImageAction(newRequest, imageLoader)
    return rememberImageActionPainter(action, filterQuality)
}
