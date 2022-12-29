package com.seiko.imageloader.component.decoder

import androidx.compose.ui.graphics.painter.Painter
import com.seiko.imageloader.Image

sealed interface DecodeResult

class DecodePainterResult(
    val painter: Painter,
) : DecodeResult

class DecodeImageResult(
    val image: Image,
) : DecodeResult
