package com.seiko.imageloader

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap

actual typealias Bitmap = org.jetbrains.skia.Bitmap

actual val Bitmap.size: Int
    get() = computeByteSize()

actual fun Bitmap.asImageBitmap(): ImageBitmap {
    return asComposeImageBitmap()
}
