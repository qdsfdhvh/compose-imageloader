package com.seiko.imageloader.model

fun ImageRequestBuilder.blur(radius: Int) {
    extra {
        set(KEY_BLUR_RADIUS, BlurEffects(radius))
    }
}

internal data class BlurEffects(
    val radius: Int,
)

internal const val KEY_BLUR_RADIUS = "KEY_BLUR_RADIUS"
