package com.seiko.imageloader

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap

actual typealias Bitmap = org.jetbrains.skia.Bitmap

actual val Bitmap.size: Int
    get() = computeByteSize()

actual fun Bitmap.asImageBitmap(): ImageBitmap {
    return asComposeImageBitmap()
}

actual val Bitmap.isMutable: Boolean
    get() = !this.isImmutable

actual val Bitmap.isRecycled: Boolean
    get() = this.isClosed

actual fun Bitmap.recycle() {
    close()
}
