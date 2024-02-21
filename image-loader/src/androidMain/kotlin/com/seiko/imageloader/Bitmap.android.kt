package com.seiko.imageloader

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

actual typealias Bitmap = android.graphics.Bitmap

actual val Bitmap.size: Int
    get() = allocationByteCount

actual fun Bitmap.asImageBitmap(): ImageBitmap {
    return asImageBitmap()
}
