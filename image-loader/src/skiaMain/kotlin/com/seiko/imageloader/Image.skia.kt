package com.seiko.imageloader

import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toComposeImageBitmap

actual typealias Image = org.jetbrains.skia.Image

actual fun Image.toPainter(filterQuality: FilterQuality): Painter {
    return BitmapPainter(toComposeImageBitmap())
}
