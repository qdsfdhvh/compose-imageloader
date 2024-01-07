package com.seiko.imageloader.component

import androidx.compose.ui.unit.Density
import com.seiko.imageloader.component.decoder.SvgDecoder

fun ComponentRegistryBuilder.setupSvgComponents(density: Density? = null) {
    add(SvgDecoder.Factory(density))
}
