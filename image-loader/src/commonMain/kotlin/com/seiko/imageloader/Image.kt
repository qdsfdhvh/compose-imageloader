package com.seiko.imageloader

import androidx.compose.ui.graphics.painter.Painter

expect class Image

expect fun Image.toPainter(): Painter
