package com.seiko.imageloader

import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter

actual typealias Bitmap = org.jetbrains.skia.Bitmap

internal actual val Bitmap.size: Int
    get() = height * rowBytes

actual fun Bitmap.toPainter(filterQuality: FilterQuality): Painter {
    return BitmapPainter(asComposeImageBitmap(), filterQuality = filterQuality)
}
