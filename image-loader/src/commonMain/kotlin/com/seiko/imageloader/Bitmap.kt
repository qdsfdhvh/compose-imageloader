package com.seiko.imageloader

import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.graphics.painter.Painter

expect class Bitmap

internal expect val Bitmap.size: Int

internal expect val Bitmap.identityHashCode: Int

expect fun Bitmap.toPainter(filterQuality: FilterQuality = DefaultFilterQuality): Painter
