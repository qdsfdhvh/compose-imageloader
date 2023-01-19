package com.seiko.imageloader.model

import androidx.compose.ui.graphics.painter.Painter
import com.seiko.imageloader.Bitmap
import okio.BufferedSource

sealed interface ImageResult {
    val request: ImageRequest
}

data class SourceResult(
    override val request: ImageRequest,
    val channel: BufferedSource,
    val dataSource: DataSource,
    val mimeType: String? = null,
    val metadata: Any? = null,
) : ImageResult

data class ComposeImageResult(
    override val request: ImageRequest,
    val image: Bitmap,
) : ImageResult

data class ComposePainterResult(
    override val request: ImageRequest,
    val painter: Painter,
) : ImageResult

data class ErrorResult(
    override val request: ImageRequest,
    val error: Throwable,
) : ImageResult
