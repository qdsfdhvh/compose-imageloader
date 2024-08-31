package com.seiko.imageloader.model

import androidx.compose.ui.geometry.Size
import com.seiko.imageloader.option.SizeResolver

fun ImageRequestBuilder.size(size: Size) {
    size(SizeResolver(size))
}

fun ImageRequestBuilder.size(width: Float, height: Float) {
    size(SizeResolver(Size(width, height)))
}

fun ImageRequestBuilder.size(size: Float) {
    size(SizeResolver(Size(size, size)))
}

fun ImageRequestBuilder.size(block: suspend () -> Size) {
    size(SizeResolver(block))
}
