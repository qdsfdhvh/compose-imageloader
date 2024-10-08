package com.seiko.imageloader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis
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
    imageLoader: ImageLoader,
    initialAction: ImageAction = ImageEvent.Start,
): State<ImageAction> {
    return remember(request, imageLoader) {
        imageLoader.async(request)
    }.collectAsState(initialAction)
}

@Composable
fun rememberImagePainter(
    request: ImageRequest,
    imageLoader: ImageLoader,
    filterQuality: FilterQuality = DefaultFilterQuality,
    placeholderPainter: (@Composable () -> Painter)? = null,
    errorPainter: (@Composable () -> Painter)? = null,
): Painter {
    val action by rememberImageAction(request, imageLoader)
    return rememberImageActionPainter(
        action = action,
        filterQuality = filterQuality,
        placeholderPainter = placeholderPainter,
        errorPainter = errorPainter,
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
        is ImageAction.Success -> rememberImageSuccessPainter(action, filterQuality)
        is ImageAction.Loading -> placeholderPainter?.invoke() ?: EmptyPainter
        is ImageAction.Failure -> errorPainter?.invoke() ?: EmptyPainter
    }
}

@Composable
fun rememberImageResultPainter(
    result: ImageResult,
    filterQuality: FilterQuality = DefaultFilterQuality,
    errorPainter: (@Composable () -> Painter)? = null,
): Painter {
    return when (result) {
        is ImageAction.Success -> rememberImageSuccessPainter(result, filterQuality)
        is ImageAction.Failure -> errorPainter?.invoke() ?: EmptyPainter
    }
}

@Composable
fun rememberImageSuccessPainter(
    action: ImageAction.Success,
    filterQuality: FilterQuality = DefaultFilterQuality,
): Painter {
    return when (action) {
        is ImageResult.OfPainter -> remember(action) {
            action.painter
        }
        is ImageResult.OfBitmap -> remember(action, filterQuality) {
            action.bitmap.toPainter(filterQuality)
        }
        is ImageResult.OfImage -> remember(action) {
            action.image.toPainter()
        }
    }.also { painter ->
        if (painter is AnimationPainter && painter.isPlay()) {
            LaunchedEffect(painter) {
                while (painter.nextPlay()) {
                    withFrameMillis { frameTimeMillis ->
                        painter.update(frameTimeMillis)
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
