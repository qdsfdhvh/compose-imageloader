package com.seiko.imageloader.model

import androidx.compose.ui.graphics.FilterQuality

fun ImageRequestBuilder.ninePatchCenterRect(centerRect: NinePatchCenterRect) {
    extra {
        set(KEY_NINE_PATCH_CENTER_RECT, centerRect)
    }
}

fun ImageRequestBuilder.ninePatchScale(scale: Float) {
    extra {
        set(KEY_NINE_PATCH_SCALE, scale)
    }
}

fun ImageRequestBuilder.ninePatchFilterQuality(filterQuality: FilterQuality) {
    extra {
        set(KEY_NINE_PATCH_SCALE, filterQuality)
    }
}

internal val ImageRequest.ninePatchCenterRect: NinePatchCenterRect?
    get() = extra[KEY_NINE_PATCH_CENTER_RECT] as? NinePatchCenterRect

internal val ImageRequest.ninePatchScale: Float?
    get() = extra[KEY_NINE_PATCH_SCALE] as? Float

internal val ImageRequest.ninePatchFilterQuality: FilterQuality?
    get() = extra[KEY_NINE_PATCH_FILTER_QUALITY] as? FilterQuality

private const val KEY_NINE_PATCH_CENTER_RECT = "KEY_NINE_PATCH_CENTER_RECT"
private const val KEY_NINE_PATCH_SCALE = "KEY_NINE_PATCH_SCALE"
private const val KEY_NINE_PATCH_FILTER_QUALITY = "KEY_NINE_PATCH_FILTER_QUALITY"
