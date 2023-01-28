package com.seiko.imageloader

import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter

expect class Bitmap

internal expect val Bitmap.size: Int

internal expect val Bitmap.identityHashCode: Int

expect fun Bitmap.asImageBitmap(): ImageBitmap

fun Bitmap.toPainter(filterQuality: FilterQuality = DefaultFilterQuality): Painter {
    return BitmapPainter(asImageBitmap(), filterQuality = filterQuality)
}
