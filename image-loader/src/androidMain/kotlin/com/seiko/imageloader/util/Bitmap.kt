package com.seiko.imageloader.util

import android.graphics.Bitmap
import android.os.Build
import com.seiko.imageloader.request.Options
import io.github.aakira.napier.Napier

internal val Bitmap.safeConfig: Bitmap.Config
    get() = config ?: Bitmap.Config.ARGB_8888

internal val Bitmap.Config.isHardware: Boolean
    get() = Build.VERSION.SDK_INT >= 26 && this == Bitmap.Config.HARDWARE

internal fun Options.ImageConfig.toBitmapConfig(): Bitmap.Config = when (this) {
    Options.ImageConfig.ALPHA_8 -> Bitmap.Config.ALPHA_8
    Options.ImageConfig.ARGB_8888 -> Bitmap.Config.ARGB_8888
    Options.ImageConfig.RGBA_F16 -> if (Build.VERSION.SDK_INT >= 26) {
        Bitmap.Config.RGBA_F16
    } else {
        Napier.w { "ImageConfig.RGBA_F16 not support in android less than API 26" }
        Bitmap.Config.ARGB_8888
    }
    Options.ImageConfig.HARDWARE -> if (Build.VERSION.SDK_INT >= 26) {
        Bitmap.Config.HARDWARE
    } else {
        Napier.w { "ImageConfig.HARDWARE not support in android less than API 26" }
        Bitmap.Config.ARGB_8888
    }
}

internal fun Bitmap.Config?.toSoftware(): Bitmap.Config {
    return if (this == null || isHardware) Bitmap.Config.ARGB_8888 else this
}
