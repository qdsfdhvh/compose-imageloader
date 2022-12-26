package com.seiko.imageloader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.RememberObserver
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
import com.seiko.imageloader.request.ComposeImageResult
import com.seiko.imageloader.request.ComposePainterResult
import com.seiko.imageloader.request.ErrorResult
import com.seiko.imageloader.request.ImageRequest
import com.seiko.imageloader.request.ImageRequestBuilder
import com.seiko.imageloader.request.ImageResult
import com.seiko.imageloader.request.SourceResult
import com.seiko.imageloader.size.Scale
import com.seiko.imageloader.util.logw
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

@Composable
fun rememberAsyncImagePainter(
    url: String,
    imageLoader: ImageLoader = LocalImageLoader.current,
    contentScale: ContentScale = ContentScale.Fit,
    filterQuality: FilterQuality = DefaultFilterQuality,
): AsyncImagePainter {
    val request = remember(url) { ImageRequestBuilder().data(url).build() }
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
    val request = remember(resId) { ImageRequestBuilder().data(resId).build() }
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

class AsyncImagePainter(
    request: ImageRequest,
    imageLoader: ImageLoader,
) : Painter(), RememberObserver {

    private var rememberJob: Job? = null
    private val drawSize = MutableStateFlow(Size.Zero)

    private var painter: Painter? by mutableStateOf(null)
    private var alpha: Float by mutableStateOf(DefaultAlpha)
    private var colorFilter: ColorFilter? by mutableStateOf(null)

    internal var contentScale = ContentScale.Fit

    internal var filterQuality = DefaultFilterQuality

    var requestState: ImageRequestState by mutableStateOf(ImageRequestState.Loading)

    var request: ImageRequest by mutableStateOf(request)
        internal set

    var imageLoader: ImageLoader by mutableStateOf(imageLoader)
        internal set

    override val intrinsicSize: Size
        get() = painter?.intrinsicSize ?: Size.Unspecified

    override fun DrawScope.onDraw() {
        // Update the draw scope's current size.
        drawSize.value = size

        // Draw the current painter.
        painter?.apply { draw(size, alpha, colorFilter) }
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
            .onStart { requestState = ImageRequestState.Loading }
            .mapLatest { imageLoader.execute(updateRequest(request)) }
            .onEach(::updateImage)
            .launchIn(imageLoader.imageScope)
    }

    override fun onForgotten() {
        clear()
        (painter as? RememberObserver)?.onForgotten()
    }

    override fun onAbandoned() {
        clear()
        (painter as? RememberObserver)?.onAbandoned()
    }

    private fun clear() {
        rememberJob?.cancel()
        rememberJob = null
    }

    private fun updateRequest(request: ImageRequest): ImageRequest {
        return request.newBuilder()
            .apply {
                if (request.scale == null) {
                    scale(contentScale.toScale())
                }
            }
            .build()
    }

    private fun updateImage(input: ImageResult) {
        requestState = when (input) {
            is ComposePainterResult -> {
                updatePainter(input.painter)
                ImageRequestState.Success
            }
            is ComposeImageResult -> {
                updatePainter(input.image.toPainter(filterQuality))
                ImageRequestState.Success
            }
            is ErrorResult -> {
                logw(
                    tag = "AsyncImagePainter",
                    data = request.data,
                    throwable = input.error,
                ) { "load image error" }
                ImageRequestState.Failure(input.error)
            }
            is SourceResult -> return
        }
    }

    private fun updatePainter(painter: Painter) {
        val previous = this.painter
        this.painter = painter
        if (rememberJob != null && previous != painter) {
            (previous as? RememberObserver)?.onForgotten()
            (painter as? RememberObserver)?.onRemembered()
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
    object Loading : ImageRequestState
}
