package com.seiko.imageloader.request

import androidx.compose.ui.graphics.painter.Painter
import com.seiko.imageloader.Image
import io.ktor.utils.io.ByteReadChannel

sealed interface ImageResult {
    val request: ImageRequest
}

data class SourceResult(
    override val request: ImageRequest,
    val channel: ByteReadChannel,
    val mimeType: String? = null,
    val metadata: Any? = null,
) : ImageResult

data class ComposeImageResult(
    override val request: ImageRequest,
    val image: Image,
) : ImageResult

data class ComposePainterResult(
    override val request: ImageRequest,
    val painter: Painter,
) : ImageResult

data class ErrorResult(
    override val request: ImageRequest,
    val error: Throwable,
) : ImageResult
