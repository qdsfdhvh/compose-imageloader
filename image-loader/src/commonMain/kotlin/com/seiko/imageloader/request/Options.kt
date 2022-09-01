package com.seiko.imageloader.request

import com.seiko.imageloader.cache.CachePolicy
import com.seiko.imageloader.size.Scale

class Options(
    val allowInexactSize: Boolean = false,
    val premultipliedAlpha: Boolean = true,
    val config: ImageConfig = ImageConfig.ARGB_8888,
    val scale: Scale = Scale.FIT,
    val memoryCachePolicy: CachePolicy = CachePolicy.ENABLED,
    val diskCachePolicy: CachePolicy = CachePolicy.ENABLED,
) {
    enum class ImageConfig {
        ALPHA_8,
        // RGB_565,
        // ARGB_4444,
        ARGB_8888,
        RGBA_F16,
        HARDWARE;
    }
}
