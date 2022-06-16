package com.seiko.imageloader.component.decoder

import androidx.compose.ui.graphics.painter.Painter
import com.seiko.imageloader.Image

sealed interface DecoderResult

class DecodePainterResult(
    val painter: Painter,
) : DecoderResult

class DecodeImageResult(
    val image: Image,
) : DecoderResult
