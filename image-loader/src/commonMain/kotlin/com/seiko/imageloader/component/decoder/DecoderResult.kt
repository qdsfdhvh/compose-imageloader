package com.seiko.imageloader.component.decoder

import androidx.compose.ui.graphics.painter.Painter

sealed interface DecoderResult

class PainterResult(
    val painter: Painter,
) : DecoderResult