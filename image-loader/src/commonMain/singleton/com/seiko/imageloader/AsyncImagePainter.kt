package com.seiko.imageloader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import com.seiko.imageloader.model.ImageEvent
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.option.Scale
import com.seiko.imageloader.util.w
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun rememberAsyncImagePainter(
    url: String,
    imageLoader: ImageLoader = LocalImageLoader.current,
    contentScale: ContentScale = ContentScale.Fit,
    filterQuality: FilterQuality = DefaultFilterQuality,
): AsyncImagePainter {
    val request = remember(url) { ImageRequest(url) }
    return rememberAsyncImagePainter(
        request = request,
        imageLoader = imageLoader,
        contentScale = contentScale,
        filterQuality = filterQuality,
    )
}

@Composable
fun rememberAsyncImagePainter(
    resId: Int,
    imageLoader: ImageLoader = LocalImageLoader.current,
    contentScale: ContentScale = ContentScale.Fit,
    filterQuality: FilterQuality = DefaultFilterQuality,
): AsyncImagePainter {
    val request = remember(resId) { ImageRequest(resId) }
    return rememberAsyncImagePainter(
        request = request,
        imageLoader = imageLoader,
        contentScale = contentScale,
        filterQuality = filterQuality,
    )
}

@Composable
fun rememberAsyncImagePainter(
    request: ImageRequest,
    imageLoader: ImageLoader = LocalImageLoader.current,
    contentScale: ContentScale = ContentScale.Fit,
    filterQuality: FilterQuality = DefaultFilterQuality,
): AsyncImagePainter {
    val painter = remember { AsyncImagePainter(request, imageLoader) }
    painter.imageLoader = imageLoader
    painter.request = request
    painter.contentScale = contentScale
    painter.filterQuality = filterQuality
    return painter
}

@Composable
fun rememberImagePainter(
    url: String,
    imageLoader: ImageLoader = LocalImageLoader.current,
    contentScale: ContentScale = ContentScale.Fit,
    filterQuality: FilterQuality = DefaultFilterQuality,
    placeholderPainter: (@Composable () -> Painter)? = null,
    errorPainter: (@Composable () -> Painter)? = null,
): Painter {
    val request = remember(url) {
        ImageRequest {
            data(url)
            if (placeholderPainter != null) {
                placeholderPainter { placeholderPainter() }
            }
            if (errorPainter != null) {
                errorPainter { errorPainter() }
            }
        }
    }
    return rememberImagePainter(
        request = request,
        imageLoader = imageLoader,
        contentScale = contentScale,
        filterQuality = filterQuality,
    )
}

@Composable
fun rememberImagePainter(
    resId: Int,
    imageLoader: ImageLoader = LocalImageLoader.current,
    contentScale: ContentScale = ContentScale.Fit,
    filterQuality: FilterQuality = DefaultFilterQuality,
    placeholderPainter: (@Composable () -> Painter)? = null,
    errorPainter: (@Composable () -> Painter)? = null,
): Painter {
    val request = remember(resId) {
        ImageRequest {
            data(resId)
            if (placeholderPainter != null) {
                placeholderPainter { placeholderPainter() }
            }
            if (errorPainter != null) {
                errorPainter { errorPainter() }
            }
        }
    }
    return rememberImagePainter(
        request = request,
        imageLoader = imageLoader,
        contentScale = contentScale,
        filterQuality = filterQuality,
    )
}

@Composable
fun rememberImagePainter(
    request: ImageRequest,
    imageLoader: ImageLoader = LocalImageLoader.current,
    contentScale: ContentScale = ContentScale.Fit,
    filterQuality: FilterQuality = DefaultFilterQuality,
): Painter {
    val painter = remember { AsyncImagePainter(request, imageLoader) }
    painter.imageLoader = imageLoader
    painter.request = request
    painter.contentScale = contentScale
    painter.filterQuality = filterQuality
    return when (painter.requestState) {
        is ImageRequestState.Loading -> request.placeholderPainter?.invoke() ?: painter
        is ImageRequestState.Failure -> request.errorPainter?.invoke() ?: painter
        else -> painter
    }
}

@Stable
class AsyncImagePainter(
    request: ImageRequest,
    imageLoader: ImageLoader,
) : Painter(), RememberObserver {

    private var rememberJob: Job? = null
    private val drawSize = MutableStateFlow(Size.Zero)

    private var painter: Painter by mutableStateOf(EmptyPainter)
    private var alpha: Float by mutableStateOf(DefaultAlpha)
    private var colorFilter: ColorFilter? by mutableStateOf(null)

    internal var contentScale = ContentScale.Fit

    internal var filterQuality = DefaultFilterQuality

    var requestState: ImageRequestState by mutableStateOf(ImageRequestState.Loading())

    var request: ImageRequest by mutableStateOf(request)
        internal set

    var imageLoader: ImageLoader by mutableStateOf(imageLoader)
        internal set

    override val intrinsicSize: Size
        get() = painter.intrinsicSize

    override fun DrawScope.onDraw() {
        // Update the draw scope's current size.
        drawSize.value = size

        // Draw the current painter.
        with(painter) {
            draw(size, alpha, colorFilter)
        }
    }

    override fun applyAlpha(alpha: Float): Boolean {
        this.alpha = alpha
        return true
    }

    override fun applyColorFilter(colorFilter: ColorFilter?): Boolean {
        this.colorFilter = colorFilter
        return true
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onRemembered() {
        // Short circuit if we're already remembered.
        if (rememberJob != null) return

        // Manually notify the child painter that we're remembered.
        (painter as? RememberObserver)?.onRemembered()

        rememberJob = snapshotFlow { request }
            .flatMapLatest { imageLoader.async(updateRequest(request)) }
            .onEach { action ->
                when (action) {
                    is ImageEvent -> requestState = ImageRequestState.Loading(action)
                    is ImageResult -> updateImageResult(action)
                }
            }
            .launchIn(imageLoader.config.imageScope)
    }

    override fun onForgotten() {
        clear()
        (painter as? RememberObserver)?.onForgotten()
        painter = EmptyPainter
    }

    override fun onAbandoned() {
        clear()
        (painter as? RememberObserver)?.onAbandoned()
        painter = EmptyPainter
    }

    private fun clear() {
        rememberJob?.cancel()
        rememberJob = null
    }

    private fun updateRequest(request: ImageRequest): ImageRequest {
        return request.newBuilder {
            options {
                if (scale == Scale.AUTO) {
                    scale = contentScale.toScale()
                }
            }
        }
    }

    private fun updateImageResult(input: ImageResult) {
        requestState = when (input) {
            is ImageResult.Bitmap -> {
                updatePainter(input.bitmap.toPainter(filterQuality))
                ImageRequestState.Success
            }
            is ImageResult.Image -> {
                updatePainter(input.image.toPainter(filterQuality))
                ImageRequestState.Success
            }
            is ImageResult.Painter -> {
                updatePainter(input.painter)
                ImageRequestState.Success
            }
            is ImageResult.Error -> {
                logAndReturnState(input.error)
            }
            is ImageResult.Source -> {
                logAndReturnState(RuntimeException("image result is source"))
            }
        }
    }

    private fun logAndReturnState(error: Throwable): ImageRequestState.Failure {
        imageLoader.config.logger.w(
            tag = "AsyncImagePainter",
            data = request.data,
            throwable = error,
        ) { "load image error" }
        return ImageRequestState.Failure(error)
    }

    private fun updatePainter(painter: Painter) {
        val previous = this.painter
        this.painter = painter
        if (previous != painter) {
            (previous as? RememberObserver)?.onForgotten()
            if (rememberJob != null) {
                (painter as? RememberObserver)?.onRemembered()
            }
        }
    }
}

private fun ContentScale.toScale() = when (this) {
    ContentScale.Fit, ContentScale.Inside -> Scale.FIT
    else -> Scale.FILL
}

@Immutable
sealed interface ImageRequestState {
    @Immutable
    object Success : ImageRequestState

    @Immutable
    data class Failure(val error: Throwable) : ImageRequestState

    @Immutable
    data class Loading(val event: ImageEvent = ImageEvent.Start) : ImageRequestState
}

private object EmptyPainter : Painter() {
    override val intrinsicSize get() = Size.Unspecified
    override fun DrawScope.onDraw() = Unit
}
