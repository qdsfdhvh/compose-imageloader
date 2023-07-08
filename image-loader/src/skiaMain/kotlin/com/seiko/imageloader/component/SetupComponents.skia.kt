package com.seiko.imageloader.component

import androidx.compose.ui.unit.Density
import com.seiko.imageloader.component.decoder.GifDecoder
import com.seiko.imageloader.component.decoder.SkiaImageDecoder
import com.seiko.imageloader.component.decoder.SvgDecoder

fun ComponentRegistryBuilder.setupSkiaComponents(
    density: Density = Density(2f),
) {
    // Decoders
    add(SvgDecoder.Factory(density))
    add(GifDecoder.Factory())
    add(SkiaImageDecoder.Factory())
}
