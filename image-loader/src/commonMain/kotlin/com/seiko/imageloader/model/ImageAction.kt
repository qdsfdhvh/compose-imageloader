package com.seiko.imageloader.model

import okio.BufferedSource

sealed interface ImageAction

sealed interface ImageEvent : ImageAction {
    object Start : ImageEvent
    object StartWithMemory : ImageEvent
    object StartWithDisk : ImageEvent
    object StartWithFetch : ImageEvent
    data class Progress(val progress: Float) : ImageEvent
}

sealed interface ImageResult : ImageAction {

    data class Source(
        val source: BufferedSource,
        val dataSource: DataSource,
        val extra: ExtraData = EmptyExtraData,
    ) : ImageResult

    data class Bitmap(
        val bitmap: com.seiko.imageloader.Bitmap,
    ) : ImageResult

    data class Image(
        val image: com.seiko.imageloader.Image,
    ) : ImageResult

    data class Painter(
        val painter: androidx.compose.ui.graphics.painter.Painter,
    ) : ImageResult

    data class Error(
        val error: Throwable,
    ) : ImageResult
}
