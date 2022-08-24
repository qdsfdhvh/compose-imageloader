package com.seiko.imageloader

import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter

actual typealias Image = org.jetbrains.skia.Bitmap

internal actual val Image.size: Int
    get() = height * rowBytes

actual fun Image.toPainter(filterQuality: FilterQuality): Painter {
    return BitmapPainter(asComposeImageBitmap(), filterQuality = filterQuality)
}
