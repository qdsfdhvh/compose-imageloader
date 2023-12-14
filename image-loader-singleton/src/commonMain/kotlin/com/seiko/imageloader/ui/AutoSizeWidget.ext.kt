package com.seiko.imageloader.ui

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.model.ImageAction
import com.seiko.imageloader.model.ImageRequest

@Composable
fun AutoSizeBox(
    url: String,
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader = LocalImageLoader.current,
    contentAlignment: Alignment = Alignment.Center,
    propagateMinConstraints: Boolean = false,
    isOnlyPostFirstEvent: Boolean = true,
    content: @Composable BoxScope.(ImageAction) -> Unit,
) {
    AutoSizeBox(
        request = remember(url) { ImageRequest(url) },
        modifier = modifier,
        imageLoader = imageLoader,
        contentAlignment = contentAlignment,
        propagateMinConstraints = propagateMinConstraints,
        isOnlyPostFirstEvent = isOnlyPostFirstEvent,
        content = content,
    )
}

@Composable
fun AutoSizeBox(
    resId: Int,
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader = LocalImageLoader.current,
    contentAlignment: Alignment = Alignment.Center,
    propagateMinConstraints: Boolean = false,
    isOnlyPostFirstEvent: Boolean = true,
    content: @Composable BoxScope.(ImageAction) -> Unit,
) {
    AutoSizeBox(
        request = remember(resId) { ImageRequest(resId) },
        modifier = modifier,
        imageLoader = imageLoader,
        contentAlignment = contentAlignment,
        propagateMinConstraints = propagateMinConstraints,
        isOnlyPostFirstEvent = isOnlyPostFirstEvent,
        content = content,
    )
}

@Composable
fun AutoSizeImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    imageLoader: ImageLoader = LocalImageLoader.current,
    placeholderPainter: (@Composable () -> Painter)? = null,
    errorPainter: (@Composable () -> Painter)? = null,
    isOnlyPostFirstEvent: Boolean = true,
) {
    AutoSizeImage(
        request = remember(url) { ImageRequest(url) },
        contentDescription = contentDescription,
        modifier = modifier,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter,
        imageLoader = imageLoader,
        placeholderPainter = placeholderPainter,
        errorPainter = errorPainter,
        isOnlyPostFirstEvent = isOnlyPostFirstEvent,
    )
}

@Composable
fun AutoSizeImage(
    resId: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    imageLoader: ImageLoader = LocalImageLoader.current,
    placeholderPainter: (@Composable () -> Painter)? = null,
    errorPainter: (@Composable () -> Painter)? = null,
    isOnlyPostFirstEvent: Boolean = true,
) {
    AutoSizeImage(
        request = remember(resId) { ImageRequest(resId) },
        contentDescription = contentDescription,
        modifier = modifier,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter,
        imageLoader = imageLoader,
        placeholderPainter = placeholderPainter,
        errorPainter = errorPainter,
        isOnlyPostFirstEvent = isOnlyPostFirstEvent,
    )
}
