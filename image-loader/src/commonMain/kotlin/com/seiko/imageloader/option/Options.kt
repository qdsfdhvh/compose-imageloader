package com.seiko.imageloader.option

import com.seiko.imageloader.cache.CachePolicy

data class Options(
    var allowInexactSize: Boolean = false,
    var premultipliedAlpha: Boolean = true,
    var retryIfDiskDecodeError: Boolean = true,
    var config: ImageConfig = ImageConfig.ARGB_8888,
    var scale: Scale = Scale.FIT,
    var sizeResolver: SizeResolver = SizeResolver.Unspecified,
    var memoryCachePolicy: CachePolicy = CachePolicy.ENABLED,
    var diskCachePolicy: CachePolicy = CachePolicy.ENABLED,
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
