package com.seiko.imageloader.util

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.MediaMetadataRetriever.BitmapParams
import android.os.Build.VERSION.SDK_INT
import androidx.annotation.RequiresApi

/** [MediaMetadataRetriever] doesn't implement [AutoCloseable] until API 29. */
internal inline fun <T> MediaMetadataRetriever.use(block: (MediaMetadataRetriever) -> T): T {
    try {
        return block(this)
    } finally {
        // We must call 'close' on API 29+ to avoid a strict mode warning.
        if (SDK_INT >= 29) {
            close()
        } else {
            release()
        }
    }
}

internal fun MediaMetadataRetriever.getFrameAtTimeCompat(
    timeUs: Long,
    option: Int,
    config: Bitmap.Config,
): Bitmap? = if (SDK_INT >= 30) {
    val params = BitmapParams().apply { preferredConfig = config }
    getFrameAtTime(timeUs, option, params)
} else {
    getFrameAtTime(timeUs, option)
}

@RequiresApi(27)
internal fun MediaMetadataRetriever.getScaledFrameAtTimeCompat(
    timeUs: Long,
    option: Int,
    dstWidth: Int,
    dstHeight: Int,
    config: Bitmap.Config,
): Bitmap? = if (SDK_INT >= 30) {
    val params = BitmapParams().apply { preferredConfig = config }
    getScaledFrameAtTime(timeUs, option, dstWidth, dstHeight, params)
} else {
    getScaledFrameAtTime(timeUs, option, dstWidth, dstHeight)
}

@RequiresApi(28)
internal fun MediaMetadataRetriever.getFrameAtIndexCompat(
    frameIndex: Int,
    config: Bitmap.Config,
): Bitmap? = getFrameAtIndex(frameIndex, BitmapParams().apply { preferredConfig = config })
