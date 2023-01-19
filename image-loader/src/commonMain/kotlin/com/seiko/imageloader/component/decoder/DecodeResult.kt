package com.seiko.imageloader.component.decoder

import androidx.compose.ui.graphics.painter.Painter
import com.seiko.imageloader.Bitmap

sealed interface DecodeResult
class DecodePainterResult(
    val painter: Painter,
) : DecodeResult

class DecodeImageResult(
    val image: Bitmap,
) : DecodeResult
