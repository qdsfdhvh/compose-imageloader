package com.seiko.imageloader.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter
import com.seiko.imageloader.Bitmap
import com.seiko.imageloader.Image
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
    class OfSource(
        val source: BufferedSource,
        val dataSource: DataSource,
        val extra: ExtraData = EmptyExtraData,
    ) : ImageResult

    @Immutable
    @Poko
    class OfBitmap(val bitmap: Bitmap) : ImageResult

    @Immutable
    @Poko
    class OfImage(val image: Image) : ImageResult

    @Immutable
    @Poko
    class OfPainter(val painter: Painter) : ImageResult

    @Immutable
    @Poko
    class OfError(val error: Throwable) : ImageResult
}
