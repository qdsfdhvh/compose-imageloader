package com.seiko.imageloader.model

import android.media.MediaMetadataRetriever.OPTION_CLOSEST
import android.media.MediaMetadataRetriever.OPTION_CLOSEST_SYNC
import android.media.MediaMetadataRetriever.OPTION_NEXT_SYNC
import android.media.MediaMetadataRetriever.OPTION_PREVIOUS_SYNC
import com.seiko.imageloader.option.Options

fun ImageRequestBuilder.videoFrameIndex(frameIndex: Int) {
    require(frameIndex >= 0) { "frameIndex$frameIndex must be >= 0." }
    // TODO: memory cache key extra
    options {
        extra {
            set(KEY_VIDEO_FRAME_INDEX, frameIndex)
        }
    }
}

fun ImageRequestBuilder.videoFrameMillis(frameMillis: Long) {
    videoFrameMicros(1000 * frameMillis)
}

fun ImageRequestBuilder.videoFrameMicros(frameMicros: Long) {
    require(frameMicros >= 0) { "frameMicros($frameMicros) must be >= 0." }
    // TODO: memory cache key extra
    options {
        extra {
            set(KEY_VIDEO_FRAME_MICROS, frameMicros)
        }
    }
}

fun ImageRequestBuilder.videoFramePercent(framePercent: Float) {
    require(framePercent in 0f..1f) { "framePercent($framePercent) must be in the range [0, 1]." }
    // TODO: memory cache key extra
    options {
        extra {
            set(KEY_VIDEO_FRAME_PERCENT, framePercent)
        }
    }
}

fun ImageRequestBuilder.videoFrameOption(option: Int) = apply {
    require(
        option == OPTION_PREVIOUS_SYNC ||
            option == OPTION_NEXT_SYNC ||
            option == OPTION_CLOSEST_SYNC ||
            option == OPTION_CLOSEST,
    ) { "Invalid video frame option: $option." }
    // TODO: memory cache key extra
    options {
        extra {
            set(KEY_VIDEO_FRAME_OPTION, option)
        }
    }
}

internal val Options.videoFrameIndex: Int
    get() = extra[KEY_VIDEO_FRAME_INDEX] as? Int ?: 0

internal val Options.videoFrameMicros: Long
    get() = extra[KEY_VIDEO_FRAME_MICROS] as? Long ?: -1

internal val Options.videoFramePercent: Float
    get() = extra[KEY_VIDEO_FRAME_PERCENT] as? Float ?: -1f

internal val Options.videoFrameOption: Int
    get() = extra[KEY_VIDEO_FRAME_OPTION] as? Int ?: OPTION_CLOSEST_SYNC

private const val KEY_VIDEO_FRAME_INDEX = "KEY_VIDEO_FRAME_INDEX"
private const val KEY_VIDEO_FRAME_MICROS = "KEY_VIDEO_FRAME_MICROS"
private const val KEY_VIDEO_FRAME_PERCENT = "KEY_VIDEO_FRAME_PERCENT"
private const val KEY_VIDEO_FRAME_OPTION = "KEY_VIDEO_FRAME_OPTION"
