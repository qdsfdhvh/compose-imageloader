package com.seiko.imageloader.model

import androidx.compose.runtime.Immutable
import com.seiko.imageloader.Poko
import okio.BufferedSource

@Immutable
sealed interface ImageAction {
    sealed interface Loading : ImageAction
    sealed interface Success : ImageAction
    sealed interface Failure : ImageAction {
        val error: Throwable
    }
}

@Immutable
sealed interface ImageEvent : ImageAction.Loading {
    data object Start : ImageEvent
    data object StartWithMemory : ImageEvent
    data object StartWithDisk : ImageEvent
    data object StartWithFetch : ImageEvent
}

@Immutable
sealed interface ImageResult : ImageAction {

    @Immutable
    @Poko
    class Bitmap(
        val bitmap: com.seiko.imageloader.Bitmap,
    ) : ImageResult, ImageAction.Success

    @Immutable
    @Poko
    class Image(
        val image: com.seiko.imageloader.Image,
    ) : ImageResult, ImageAction.Success

    @Immutable
    @Poko
    class Painter(
        val painter: androidx.compose.ui.graphics.painter.Painter,
    ) : ImageResult, ImageAction.Success

    @Immutable
    @Poko
    class Error(
        override val error: Throwable,
    ) : ImageResult, ImageAction.Failure

    @Immutable
    @Poko
    class Source(
        val source: BufferedSource,
        val dataSource: DataSource,
        val extra: ExtraData = EmptyExtraData,
    ) : ImageResult, ImageAction.Failure {
        override val error: Throwable
            get() = IllegalStateException("failure to decode image source")
    }
}
