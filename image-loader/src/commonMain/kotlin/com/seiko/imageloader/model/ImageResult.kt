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

    data class Image(
        override val request: ImageRequest,
        val image: com.seiko.imageloader.Image,
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
