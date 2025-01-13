package com.seiko.imageloader.option

import androidx.compose.ui.layout.ContentScale

enum class Scale {
    /**
     * Fill the image in the view such that both dimensions (width and height) of the image will be
     * **equal to or larger than** the corresponding dimension of the view.
     */
    FILL,

    /**
     * Fit the image to the view so that both dimensions (width and height) of the image will be
     * **equal to or less than** the corresponding dimension of the view.
     *
     * Generally, this is the default value for functions that accept a [Scale].
     */
    FIT,
}

fun ContentScale.toScale() = when (this) {
    ContentScale.Fit, ContentScale.Inside -> Scale.FIT
    else -> Scale.FILL
}
