package com.seiko.imageloader

import android.content.Context
import android.os.Build
import com.seiko.imageloader.component.decoder.BitmapFactoryDecoder
import com.seiko.imageloader.component.decoder.GifDecoder
import com.seiko.imageloader.component.decoder.ImageDecoderDecoder
import com.seiko.imageloader.component.decoder.SvgDecoder
import com.seiko.imageloader.component.fetcher.AssetUriFetcher
import com.seiko.imageloader.component.fetcher.Base64Fetcher
import com.seiko.imageloader.component.fetcher.BitmapFetcher
import com.seiko.imageloader.component.fetcher.ByteBufferFetcher
import com.seiko.imageloader.component.fetcher.ContentUriFetcher
import com.seiko.imageloader.component.fetcher.DrawableFetcher
import com.seiko.imageloader.component.fetcher.FileFetcher
import com.seiko.imageloader.component.fetcher.KtorUrlFetcher
import com.seiko.imageloader.component.fetcher.ResourceUriFetcher
import com.seiko.imageloader.component.keyer.FileKeyer
import com.seiko.imageloader.component.keyer.KtorUrlKeyer
import com.seiko.imageloader.component.keyer.UriKeyer
import com.seiko.imageloader.component.mapper.Base64Mapper
import com.seiko.imageloader.component.mapper.ByteArrayMapper
import com.seiko.imageloader.component.mapper.FileUriMapper
import com.seiko.imageloader.component.mapper.KtorUrlMapper
import com.seiko.imageloader.component.mapper.ResourceIntMapper
import com.seiko.imageloader.component.mapper.ResourceUriMapper
import com.seiko.imageloader.component.mapper.StringUriMapper
import com.seiko.imageloader.request.Options

actual class ImageLoaderBuilder constructor(
    context: Context,
    private val maxImageSize: Int = 4096
) : CommonImageLoaderBuilder<ImageLoaderBuilder>() {

    private val context = context.applicationContext

    actual fun build(): ImageLoader {
        val components = componentBuilder
            // Mappers
            .add(Base64Mapper())
            .add(KtorUrlMapper())
            .add(StringUriMapper())
            .add(FileUriMapper())
            .add(ResourceUriMapper(context))
            .add(ResourceIntMapper(context))
            .add(ByteArrayMapper())
            // Keyers
            .add(KtorUrlKeyer())
            .add(FileKeyer(true))
            .add(UriKeyer(context))
            // Fetchers
            .add(Base64Fetcher.Factory())
            .add(KtorUrlFetcher.Factory(httpClient))
            .add(FileFetcher.Factory())
            .add(AssetUriFetcher.Factory(context))
            .add(ContentUriFetcher.Factory(context))
            .add(ResourceUriFetcher.Factory(context))
            .add(DrawableFetcher.Factory())
            .add(BitmapFetcher.Factory())
            .add(ByteBufferFetcher.Factory())
            // Decoders
            .add(SvgDecoder.Factory())
            .add(if (Build.VERSION.SDK_INT >= 28) ImageDecoderDecoder.Factory(context) else GifDecoder.Factory())
            .add(BitmapFactoryDecoder.Factory(context, maxImageSize))
            .build()

        return RealImageLoader(
            components = components,
            options = options ?: Options(),
            requestDispatcher = requestDispatcher,
            imageScope = imageScope,
            interceptors = interceptors,
            memoryCache = memoryCache,
            diskCache = diskCache,
        )
    }
}
