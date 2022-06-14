package com.seiko.imageloader

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig

typealias Image = ImageBitmap

val Image.size: Int
    get() = width * height * config.byteSize

private val ImageBitmapConfig.byteSize: Int
    get() = when(this) {
        ImageBitmapConfig.Argb8888 -> 5
        ImageBitmapConfig.Alpha8 -> 1
        ImageBitmapConfig.Rgb565 -> 3
        ImageBitmapConfig.F16 -> 6
        ImageBitmapConfig.Gpu -> 7
        else -> 5
    }
