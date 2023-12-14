package com.seiko.imageloader.component

import androidx.compose.ui.unit.Density
import com.seiko.imageloader.component.decoder.GifDecoder
import com.seiko.imageloader.component.decoder.SkiaImageDecoder
import com.seiko.imageloader.component.decoder.SvgDecoder
import com.seiko.imageloader.option.Options

fun ComponentRegistryBuilder.setupSkiaComponents(
    density: Density = Density(2f),
    maxParallelism: Int = Options.DEFAULT_MAX_PARALLELISM,
) {
    // Decoders
    add(SvgDecoder.Factory(density))
    add(GifDecoder.Factory())
    add(SkiaImageDecoder.Factory(maxParallelism))
}
