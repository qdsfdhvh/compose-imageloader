package com.seiko.imageloader.util

import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmapConfig

internal val Bitmap.safeConfig: Bitmap.Config
    get() = config ?: Bitmap.Config.ARGB_8888

internal val Bitmap.Config.isHardware: Boolean
    get() = Build.VERSION.SDK_INT >= 26 && this == Bitmap.Config.HARDWARE

internal fun ImageBitmapConfig.toAndroidConfig(): Bitmap.Config = when (this) {
    ImageBitmapConfig.Alpha8 -> Bitmap.Config.ALPHA_8
    ImageBitmapConfig.Argb8888 -> Bitmap.Config.ARGB_8888
    ImageBitmapConfig.F16 -> if (Build.VERSION.SDK_INT >= 26) {
        Bitmap.Config.RGBA_F16
    } else {
        Log.w("ImageConfig", "ImageConfig.RGBA_F16 not support in android less than API 26")
        Bitmap.Config.ARGB_8888
    }
    ImageBitmapConfig.Gpu -> if (Build.VERSION.SDK_INT >= 26) {
        Bitmap.Config.HARDWARE
    } else {
        Log.w("ImageConfig", "ImageConfig.HARDWARE not support in android less than API 26")
        Bitmap.Config.ARGB_8888
    }
    ImageBitmapConfig.Rgb565 -> Bitmap.Config.RGB_565
    else -> Bitmap.Config.ARGB_8888
}

internal fun Bitmap.Config?.toSoftware(): Bitmap.Config {
    return if (this == null || isHardware) Bitmap.Config.ARGB_8888 else this
}
