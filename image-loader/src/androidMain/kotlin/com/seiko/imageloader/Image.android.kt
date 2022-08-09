package com.seiko.imageloader

import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter

actual typealias Image = android.graphics.Bitmap

internal actual val Image.size: Int
    get() = allocationByteCount

internal actual inline val Image.identityHashCode: Int
    get() = System.identityHashCode(this)

actual fun Image.toPainter(): Painter {
    return BitmapPainter(asImageBitmap())
}
