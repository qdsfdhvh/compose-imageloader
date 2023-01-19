package com.seiko.imageloader

import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter

actual typealias Bitmap = android.graphics.Bitmap

internal actual val Bitmap.size: Int
    get() = allocationByteCount

internal actual inline val Bitmap.identityHashCode: Int
    get() = System.identityHashCode(this)

actual fun Bitmap.toPainter(filterQuality: FilterQuality): Painter {
    return BitmapPainter(asImageBitmap(), filterQuality = filterQuality)
}
