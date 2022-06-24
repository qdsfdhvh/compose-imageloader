package com.seiko.imageloader

import androidx.compose.ui.graphics.painter.Painter

internal expect val supportImageMemoryCache: Boolean

expect class Image

internal expect val Image.size: Int

internal expect val Image.identityHashCode: Int

expect fun Image.toPainter(): Painter

// get() = width * height * config.byteSize

// private val ImageBitmapConfig.byteSize: Int
//     get() = when (this) {
//         ImageBitmapConfig.Argb8888 -> 5
//         ImageBitmapConfig.Alpha8 -> 1
//         ImageBitmapConfig.Rgb565 -> 3
//         ImageBitmapConfig.F16 -> 6
//         ImageBitmapConfig.Gpu -> 7
//         else -> 5
//     }
