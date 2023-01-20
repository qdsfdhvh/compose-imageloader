package com.seiko.imageloader

import androidx.compose.ui.unit.Density
import com.seiko.imageloader.component.decoder.GifDecoder
import com.seiko.imageloader.component.decoder.SkiaImageDecoder
import com.seiko.imageloader.component.decoder.SvgDecoder
import com.seiko.imageloader.component.fetcher.Base64Fetcher
import com.seiko.imageloader.component.fetcher.BitmapFetcher
import com.seiko.imageloader.component.fetcher.ByteBufferFetcher
import com.seiko.imageloader.component.fetcher.FileFetcher
import com.seiko.imageloader.component.fetcher.KtorUrlFetcher
import com.seiko.imageloader.component.keyer.FileKeyer
import com.seiko.imageloader.component.keyer.KtorUrlKeyer
import com.seiko.imageloader.component.mapper.Base64Mapper
import com.seiko.imageloader.component.mapper.ByteArrayMapper
import com.seiko.imageloader.component.mapper.FileUriMapper
import com.seiko.imageloader.component.mapper.KtorUrlMapper
import com.seiko.imageloader.component.mapper.StringUriMapper
import com.seiko.imageloader.option.Options

actual class ImageLoaderBuilder : CommonImageLoaderBuilder<ImageLoaderBuilder>() {

    private var density: Density? = null

    fun density(density: Density) = apply {
        this.density = density
    }

    actual fun build(): ImageLoader {
        val components = componentBuilder
            // Mappers
            .add(Base64Mapper())
            .add(KtorUrlMapper())
            .add(StringUriMapper())
            .add(FileUriMapper())
            .add(ByteArrayMapper())
            // Keyers
            .add(KtorUrlKeyer())
            .add(FileKeyer())
            // Fetchers
            .add(Base64Fetcher.Factory())
            .add(KtorUrlFetcher.Factory(httpClient))
            .add(FileFetcher.Factory())
            .add(ByteBufferFetcher.Factory())
            .add(BitmapFetcher.Factory())
            // Decoders
            .add(SvgDecoder.Factory(density ?: Density(2f)))
            .add(GifDecoder.Factory(imageScope))
            .add(SkiaImageDecoder.Factory())
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
