package com.seiko.imageloader.model

import androidx.compose.runtime.Immutable
import okio.BufferedSource

@Immutable
sealed interface ImageAction

@Immutable
sealed interface ImageEvent : ImageAction {
    object Start : ImageEvent
    object StartWithMemory : ImageEvent
    object StartWithDisk : ImageEvent
    object StartWithFetch : ImageEvent
    data class Progress(val progress: Float) : ImageEvent
}

@Immutable
sealed interface ImageResult : ImageAction {

    @Immutable
    data class Source(
        val source: BufferedSource,
        val dataSource: DataSource,
        val extra: ExtraData = EmptyExtraData,
    ) : ImageResult

    @Immutable
    data class Bitmap(
        val bitmap: com.seiko.imageloader.Bitmap,
    ) : ImageResult

    @Immutable
    data class Image(
        val image: com.seiko.imageloader.Image,
    ) : ImageResult

    @Immutable
    data class Painter(
        val painter: androidx.compose.ui.graphics.painter.Painter,
    ) : ImageResult

    @Immutable
    data class Error(
        val error: Throwable,
    ) : ImageResult
}
