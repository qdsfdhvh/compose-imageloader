package com.seiko.imageloader.request

import com.seiko.imageloader.size.Scale
import com.seiko.imageloader.size.Size

class Options(
    val allowInexactSize: Boolean = false,
    val premultipliedAlpha: Boolean = true,
    val config: ImageConfig = ImageConfig.ARGB_8888,
    val size: Size = Size.ORIGINAL,
    val scale: Scale = Scale.FIT,
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