package com.seiko.imageloader

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

actual typealias Bitmap = android.graphics.Bitmap

actual val Bitmap.size: Int
    get() {
        if (isRecycled) {
            error("Cannot obtain size for recycled Bitmap: $this [${width}x${height}] $config")
        }
        return allocationByteCount
    }

actual fun Bitmap.asImageBitmap(): ImageBitmap {
    return asImageBitmap()
}

actual val Bitmap.isMutable: Boolean
    get() = this.isMutable

actual val Bitmap.isRecycled: Boolean
    get() = this.isRecycled

actual fun Bitmap.recycle() {
    this.recycle()
}
