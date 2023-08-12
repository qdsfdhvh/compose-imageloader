package com.seiko.imageloader.model

import androidx.compose.runtime.Immutable
import com.seiko.imageloader.Poko
import okio.BufferedSource

@Immutable
sealed interface ImageAction

@Immutable
sealed interface ImageEvent : ImageAction {
    object Start : ImageEvent
    object StartWithMemory : ImageEvent
    object StartWithDisk : ImageEvent
    object StartWithFetch : ImageEvent

    @Poko class Progress(val progress: Float) : ImageEvent
}

@Immutable
sealed interface ImageResult : ImageAction {

    @Immutable
    @Poko
    class Source(
        val source: BufferedSource,
        val dataSource: DataSource,
        val extra: ExtraData = EmptyExtraData,
    ) : ImageResult

    @Immutable
    @Poko
    class Bitmap(
        val bitmap: com.seiko.imageloader.Bitmap,
    ) : ImageResult

    @Immutable
    @Poko
    class Image(
        val image: com.seiko.imageloader.Image,
    ) : ImageResult

    @Immutable
    @Poko
    class Painter(
        val painter: androidx.compose.ui.graphics.painter.Painter,
    ) : ImageResult

    @Immutable
    @Poko
    class Error(
        val error: Throwable,
    ) : ImageResult
}
