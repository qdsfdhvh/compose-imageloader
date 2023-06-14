package com.seiko.imageloader.model

fun ImageRequestBuilder.blur(radius: Int) {
    extra {
        set(KEY_BLUR_RADIUS, BlurEffects(radius))
    }
}

internal val ImageRequest.blurEffects: BlurEffects?
    get() = extra[KEY_BLUR_RADIUS] as? BlurEffects

internal data class BlurEffects(
    val radius: Int,
)

private const val KEY_BLUR_RADIUS = "KEY_BLUR_RADIUS"
