package com.seiko.imageloader

import androidx.compose.ui.graphics.painter.Painter

expect class Image

internal expect val Image.size: Int

internal expect val Image.identityHashCode: Int

expect fun Image.toPainter(): Painter
