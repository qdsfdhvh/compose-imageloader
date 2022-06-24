package com.seiko.imageloader

import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toComposeImageBitmap

internal actual val supportImageMemoryCache: Boolean = false

actual typealias Image = java.awt.image.BufferedImage

internal actual val Image.size: Int
    get() = TODO()

internal actual inline val Image.identityHashCode: Int
    get() = System.identityHashCode(this)

actual fun Image.toPainter(): Painter {
    return BitmapPainter(toComposeImageBitmap())
}
