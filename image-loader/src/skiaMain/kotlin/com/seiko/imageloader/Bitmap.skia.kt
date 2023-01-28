package com.seiko.imageloader

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap

actual typealias Bitmap = org.jetbrains.skia.Bitmap

internal actual val Bitmap.size: Int
    get() = height * rowBytes

actual fun Bitmap.asImageBitmap(): ImageBitmap {
    return asComposeImageBitmap()
}
