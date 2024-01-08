package com.seiko.imageloader.component

import com.seiko.imageloader.component.decoder.GifDecoder
import com.seiko.imageloader.component.decoder.SkiaImageDecoder
import com.seiko.imageloader.option.Options

fun ComponentRegistryBuilder.setupSkiaComponents(
    maxParallelism: Int = Options.DEFAULT_MAX_PARALLELISM,
) {
    add(GifDecoder.Factory())
    add(SkiaImageDecoder.Factory(maxParallelism))
}
