package com.seiko.imageloader.component

import android.content.Context
import android.os.Build
import com.seiko.imageloader.component.decoder.BitmapFactoryDecoder
import com.seiko.imageloader.component.decoder.GifDecoder
import com.seiko.imageloader.component.decoder.ImageDecoderDecoder
import com.seiko.imageloader.component.decoder.SvgDecoder
import com.seiko.imageloader.component.fetcher.AssetUriFetcher
import com.seiko.imageloader.component.fetcher.BitmapFetcher
import com.seiko.imageloader.component.fetcher.ContentUriFetcher
import com.seiko.imageloader.component.fetcher.DrawableFetcher
import com.seiko.imageloader.component.fetcher.ResourceUriFetcher
import com.seiko.imageloader.component.keyer.UriKeyer
import com.seiko.imageloader.component.mapper.ResourceIntMapper
import com.seiko.imageloader.component.mapper.ResourceUriMapper
import com.seiko.imageloader.component.mapper.StringUriMapper

fun ComponentRegistryBuilder.setupAndroidComponents(
    context: Context,
    maxImageSize: Int = 4096
) {
    // Mappers
    add(StringUriMapper())
    add(ResourceUriMapper(context))
    add(ResourceIntMapper(context))
    // Keyers
    add(UriKeyer(context))
    // Fetchers
    add(AssetUriFetcher.Factory(context))
    add(ContentUriFetcher.Factory(context))
    add(ResourceUriFetcher.Factory(context))
    add(DrawableFetcher.Factory())
    add(BitmapFetcher.Factory())
    // Decoders
    add(SvgDecoder.Factory())
    add(if (Build.VERSION.SDK_INT >= 28) ImageDecoderDecoder.Factory(context) else GifDecoder.Factory())
    add(BitmapFactoryDecoder.Factory(context, maxImageSize))
}
