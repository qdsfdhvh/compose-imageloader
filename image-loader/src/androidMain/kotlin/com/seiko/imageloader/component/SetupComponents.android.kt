package com.seiko.imageloader.component

import android.content.Context
import android.os.Build
import androidx.compose.ui.unit.Density
import com.seiko.imageloader.component.decoder.BitmapFactoryDecoder
import com.seiko.imageloader.component.decoder.GifDecoder
import com.seiko.imageloader.component.decoder.ImageDecoderDecoder
import com.seiko.imageloader.component.decoder.SvgDecoder
import com.seiko.imageloader.component.fetcher.AssetUriFetcher
import com.seiko.imageloader.component.fetcher.ContentUriFetcher
import com.seiko.imageloader.component.fetcher.DrawableFetcher
import com.seiko.imageloader.component.fetcher.ResourceUriFetcher
import com.seiko.imageloader.component.keyer.UriKeyer
import com.seiko.imageloader.component.mapper.ResourceIntMapper
import com.seiko.imageloader.component.mapper.ResourceUriMapper
import com.seiko.imageloader.option.Options

fun ComponentRegistryBuilder.setupAndroidComponents(
    context: Context? = null,
    density: Density? = context?.let { Density(it) },
    maxParallelism: Int = Options.DEFAULT_MAX_PARALLELISM,
) {
    // Mappers
    add(ResourceUriMapper(context))
    add(ResourceIntMapper(context))
    // Keyers
    add(UriKeyer(context))
    // Fetchers
    add(AssetUriFetcher.Factory(context))
    add(ContentUriFetcher.Factory(context))
    add(ResourceUriFetcher.Factory(context))
    add(DrawableFetcher.Factory())
    // Decoders
    add(SvgDecoder.Factory(density))
    if (Build.VERSION.SDK_INT >= 28) {
        add(ImageDecoderDecoder.Factory(context, maxParallelism))
    } else {
        add(GifDecoder.Factory())
        add(BitmapFactoryDecoder.Factory(context, maxParallelism))
    }
}
