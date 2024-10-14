package com.seiko.imageloader.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter
import com.seiko.imageloader.Bitmap
import com.seiko.imageloader.Image
import dev.drewhamilton.poko.Poko

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
    class OfBitmap(val bitmap: Bitmap) : ImageResult, ImageAction.Success

    @Immutable
    @Poko
    class OfImage(val image: Image) : ImageResult, ImageAction.Success

    @Immutable
    @Poko
    class OfPainter(val painter: Painter) : ImageResult, ImageAction.Success

    @Immutable
    @Poko
    class OfError(override val error: Throwable) : ImageResult, ImageAction.Failure

    @Immutable
    @Poko
    class OfSource(
        val imageSource: ImageSource,
        val imageSourceFrom: ImageSourceFrom,
        val extra: ExtraData = EmptyExtraData,
    ) : ImageResult, ImageAction.Failure {
        override val error: Throwable
            get() = IllegalStateException("failure to decode image source")
    }
}
