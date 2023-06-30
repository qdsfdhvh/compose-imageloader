package com.seiko.imageloader.option

import com.seiko.imageloader.cache.CachePolicy
import com.seiko.imageloader.model.EmptyExtraData
import com.seiko.imageloader.model.ExtraData
import com.seiko.imageloader.model.ExtraDataBuilder
import com.seiko.imageloader.model.extraData

data class Options internal constructor(
    val allowInexactSize: Boolean,
    val premultipliedAlpha: Boolean,
    val retryIfDiskDecodeError: Boolean,
    val imageConfig: ImageConfig,
    val scale: Scale,
    val sizeResolver: SizeResolver,
    val memoryCachePolicy: CachePolicy,
    val diskCachePolicy: CachePolicy,
    val playAnimate: Boolean,
    val repeatCount: Int,
    val extra: ExtraData,
) {
    fun newBuilder(block: OptionsBuilder.() -> Unit) =
        OptionsBuilder(this).apply(block).build()

    enum class ImageConfig {
        ALPHA_8,
        ARGB_8888,
        RGBA_F16,
        HARDWARE,
    }

    companion object {
        internal const val REPEAT_INFINITE = -1
    }
}

class OptionsBuilder {

    var allowInexactSize: Boolean
    var premultipliedAlpha: Boolean
    var retryIfDiskDecodeError: Boolean
    var imageConfig: Options.ImageConfig
    var scale: Scale
    var sizeResolver: SizeResolver
    var memoryCachePolicy: CachePolicy
    var diskCachePolicy: CachePolicy
    var playAnimate: Boolean
    private var _repeatCount: Int
    private var extraData: ExtraData?

    internal constructor() {
        allowInexactSize = false
        premultipliedAlpha = true
        retryIfDiskDecodeError = true
        imageConfig = Options.ImageConfig.ARGB_8888
        scale = Scale.FILL
        sizeResolver = SizeResolver.Unspecified
        memoryCachePolicy = CachePolicy.ENABLED
        diskCachePolicy = CachePolicy.ENABLED
        playAnimate = true
        _repeatCount = Options.REPEAT_INFINITE
        extraData = null
    }

    internal constructor(options: Options) {
        allowInexactSize = options.allowInexactSize
        premultipliedAlpha = options.premultipliedAlpha
        retryIfDiskDecodeError = options.retryIfDiskDecodeError
        imageConfig = options.imageConfig
        scale = options.scale
        sizeResolver = options.sizeResolver
        memoryCachePolicy = options.memoryCachePolicy
        diskCachePolicy = options.diskCachePolicy
        playAnimate = options.playAnimate
        _repeatCount = options.repeatCount
        extraData = options.extra
    }

    var repeatCount: Int
        get() = _repeatCount
        set(value) {
            _repeatCount = maxOf(value, Options.REPEAT_INFINITE)
        }

    fun extra(builder: ExtraDataBuilder.() -> Unit) {
        extraData = extraData
            ?.takeUnless { it.isEmpty() }
            ?.toMutableMap()
            ?.apply(builder)
            ?: extraData(builder)
    }

    internal fun build() = Options(
        allowInexactSize = allowInexactSize,
        premultipliedAlpha = premultipliedAlpha,
        retryIfDiskDecodeError = retryIfDiskDecodeError,
        imageConfig = imageConfig,
        scale = scale,
        sizeResolver = sizeResolver,
        memoryCachePolicy = memoryCachePolicy,
        diskCachePolicy = diskCachePolicy,
        playAnimate = playAnimate,
        repeatCount = repeatCount,
        extra = extraData ?: EmptyExtraData,
    )
}

fun Options(block: OptionsBuilder.() -> Unit = {}) =
    OptionsBuilder().apply(block).build()
