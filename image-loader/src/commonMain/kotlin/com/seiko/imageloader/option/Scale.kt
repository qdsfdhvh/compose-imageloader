package com.seiko.imageloader.option

import androidx.compose.ui.layout.ContentScale

enum class Scale {
    FILL,
    FIT,
}

fun ContentScale.toScale() = when (this) {
    ContentScale.Fit, ContentScale.Inside -> Scale.FIT
    else -> Scale.FILL
}
