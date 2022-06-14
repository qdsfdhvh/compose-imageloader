package com.seiko.imageloader.component.decoder

import com.seiko.imageloader.Image

sealed interface DecoderResult

class DecodeImageResult(
    val image: Image,
) : DecoderResult
