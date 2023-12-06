package com.seiko.imageloader.option

import androidx.compose.ui.geometry.Size
import com.seiko.imageloader.BitmapConfig
import com.seiko.imageloader.cache.CachePolicy
import com.seiko.imageloader.model.EmptyExtraData
import com.seiko.imageloader.model.ExtraData
import com.seiko.imageloader.model.ExtraDataBuilder
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.model.extraData
import com.seiko.imageloader.util.DEFAULT_MAX_IMAGE_SIZE
import dev.drewhamilton.poko.Poko

@Poko
class Options internal constructor(
    val allowInexactSize: Boolean,
    val premultipliedAlpha: Boolean,
    val retryIfDiskDecodeError: Boolean,
    val bitmapConfig: BitmapConfig,
    val size: Size,
    val scale: Scale,
    val memoryCachePolicy: CachePolicy,
    val diskCachePolicy: CachePolicy,
    val playAnimate: Boolean,
    val repeatCount: Int,
    val maxImageSize: Int,
    val extra: ExtraData,
) {
    companion object {
        internal const val REPEAT_INFINITE = -1
    }
}

class OptionsBuilder internal constructor() {

    var allowInexactSize: Boolean = false
    var premultipliedAlpha: Boolean = true
    var retryIfDiskDecodeError: Boolean = true
    var bitmapConfig: BitmapConfig = BitmapConfig.Default
    var size: Size = Size.Unspecified
    var scale: Scale = Scale.FILL
    var memoryCachePolicy: CachePolicy = CachePolicy.ENABLED
    var diskCachePolicy: CachePolicy = CachePolicy.ENABLED
    var playAnimate: Boolean = true
    private var _repeatCount: Int = Options.REPEAT_INFINITE
    var maxImageSize = DEFAULT_MAX_IMAGE_SIZE
    private var extraData: ExtraData? = null

    var repeatCount: Int
        get() = _repeatCount
        set(value) {
            _repeatCount = maxOf(value, Options.REPEAT_INFINITE)
        }

    fun takeFrom(
        options: Options,
        clearOptionsExtraData: Boolean = false,
    ) {
        allowInexactSize = options.allowInexactSize
        premultipliedAlpha = options.premultipliedAlpha
        retryIfDiskDecodeError = options.retryIfDiskDecodeError
        bitmapConfig = options.bitmapConfig
        size = options.size
        scale = options.scale
        memoryCachePolicy = options.memoryCachePolicy
        diskCachePolicy = options.diskCachePolicy
        playAnimate = options.playAnimate
        _repeatCount = options.repeatCount
        maxImageSize = options.maxImageSize
        extra {
            if (clearOptionsExtraData) {
                clear()
            }
            putAll(options.extra)
        }
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
        bitmapConfig = bitmapConfig,
        size = size,
        scale = scale,
        memoryCachePolicy = memoryCachePolicy,
        diskCachePolicy = diskCachePolicy,
        playAnimate = playAnimate,
        repeatCount = repeatCount,
        maxImageSize = maxImageSize,
        extra = extraData ?: EmptyExtraData,
    )
}

internal fun OptionsBuilder.takeFrom(request: ImageRequest) {
    request.optionsBuilders.forEach { builder ->
        builder.invoke(this)
    }
}

fun Options(block: OptionsBuilder.() -> Unit = {}) =
    OptionsBuilder().apply(block).build()

fun Options(options: Options, block: OptionsBuilder.() -> Unit = {}) =
    OptionsBuilder().apply {
        takeFrom(options)
        block.invoke(this)
    }.build()
