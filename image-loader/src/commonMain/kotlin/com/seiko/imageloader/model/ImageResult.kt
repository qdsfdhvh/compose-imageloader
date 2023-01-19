package com.seiko.imageloader.model

import okio.BufferedSource

sealed interface ImageResult {
    val request: ImageRequest

    data class Source(
        override val request: ImageRequest,
        val source: BufferedSource,
        val dataSource: DataSource,
        val extra: ExtraData = EmptyExtraData,
    ) : ImageResult

    data class Bitmap(
        override val request: ImageRequest,
        val bitmap: com.seiko.imageloader.Bitmap,
    ) : ImageResult

    data class Painter(
        override val request: ImageRequest,
        val painter: androidx.compose.ui.graphics.painter.Painter,
    ) : ImageResult

    data class Error(
        override val request: ImageRequest,
        val error: Throwable,
    ) : ImageResult
}

// data class SourceResult(
//     override val request: ImageRequest,
//     val channel: BufferedSource,
//     val dataSource: DataSource,
//     val mimeType: String? = null,
//     val metadata: Any? = null,
// ) : ImageResult
//
// data class ComposeImageResult(
//     override val request: ImageRequest,
//     val image: Bitmap,
// ) : ImageResult
//
// data class ComposePainterResult(
//     override val request: ImageRequest,
//     val painter: Painter,
// ) : ImageResult
//
// data class ErrorResult(
//     override val request: ImageRequest,
//     val error: Throwable,
// ) : ImageResult
