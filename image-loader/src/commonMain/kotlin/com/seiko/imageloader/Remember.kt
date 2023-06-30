package com.seiko.imageloader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.graphics.painter.Painter
import com.seiko.imageloader.model.ImageAction
import com.seiko.imageloader.model.ImageEvent
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.util.AnimationPainter

@Composable
fun rememberImageAction(
    request: ImageRequest,
    imageLoader: ImageLoader = LocalImageLoader.current,
): State<ImageAction> {
    return remember(request, imageLoader) {
        imageLoader.async(request)
    }.collectAsState(ImageEvent.Start)
}

@Composable
fun rememberImageActionPainter(
    request: ImageRequest,
    imageLoader: ImageLoader = LocalImageLoader.current,
    filterQuality: FilterQuality = DefaultFilterQuality,
): Painter {
    val action by rememberImageAction(request, imageLoader)
    return rememberImageActionPainter(
        action = action,
        filterQuality = filterQuality,
        placeholderPainter = request.placeholderPainter,
        errorPainter = request.errorPainter,
    )
}

@Composable
fun rememberImageActionPainter(
    action: ImageAction,
    filterQuality: FilterQuality = DefaultFilterQuality,
    placeholderPainter: (@Composable () -> Painter)? = null,
    errorPainter: (@Composable () -> Painter)? = null,
): Painter {
    return when (action) {
        is ImageEvent -> placeholderPainter?.invoke() ?: EmptyPainter
        is ImageResult -> rememberImageResultPainter(
            result = action,
            filterQuality = filterQuality,
            errorPainter = errorPainter,
        )
    }
}

@Composable
fun rememberImageResultPainter(
    result: ImageResult,
    filterQuality: FilterQuality = DefaultFilterQuality,
    errorPainter: (@Composable () -> Painter)? = null,
): Painter {
    return when (result) {
        is ImageResult.Painter -> remember {
            result.painter
        }
        is ImageResult.Bitmap -> remember(filterQuality) {
            result.bitmap.toPainter(filterQuality)
        }
        is ImageResult.Image -> remember(filterQuality) {
            result.image.toPainter(filterQuality)
        }
        is ImageResult.Error,
        is ImageResult.Source,
        -> errorPainter?.invoke() ?: EmptyPainter
    }.also { painter ->
        if (painter is AnimationPainter) {
            LaunchedEffect(painter) {
                while (painter.isPlay()) {
                    withFrameNanos { nanoTime ->
                        painter.update(nanoTime)
                    }
                }
            }
        }
    }
}

object EmptyPainter : Painter() {
    override val intrinsicSize get() = Size.Unspecified
    override fun DrawScope.onDraw() = Unit
}
