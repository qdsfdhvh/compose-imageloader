package com.seiko.imageloader

import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter

internal actual val supportImageMemoryCache: Boolean = true

actual typealias Image = org.jetbrains.skia.Bitmap

internal actual val Image.size: Int
    get() = height * rowBytes

actual fun Image.toPainter(): Painter {
    return BitmapPainter(asComposeImageBitmap())
}
