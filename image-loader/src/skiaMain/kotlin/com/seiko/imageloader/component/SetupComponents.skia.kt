package com.seiko.imageloader.component

import androidx.compose.ui.unit.Density
import com.seiko.imageloader.component.decoder.GifDecoder
import com.seiko.imageloader.component.decoder.SkiaImageDecoder
import com.seiko.imageloader.component.decoder.SvgDecoder
import kotlinx.coroutines.CoroutineScope

fun ComponentRegistryBuilder.setupSkiaComponents(
    imageScope: CoroutineScope,
    density: Density = Density(2f),
) {
    // Decoders
    add(SvgDecoder.Factory(density))
    add(GifDecoder.Factory(imageScope))
    add(SkiaImageDecoder.Factory())
}