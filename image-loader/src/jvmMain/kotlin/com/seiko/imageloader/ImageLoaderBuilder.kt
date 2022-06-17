package com.seiko.imageloader

import com.seiko.imageloader.component.decoder.GifDecoder
import com.seiko.imageloader.component.decoder.ImageIODecoder
import com.seiko.imageloader.component.fetcher.FileFetcher
import com.seiko.imageloader.component.fetcher.KtorUrlFetcher
import com.seiko.imageloader.component.keyer.KtorUlKeyer
import com.seiko.imageloader.component.mapper.KtorUrlMapper
import com.seiko.imageloader.request.Options
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual class ImageLoaderBuilder : CommonImageLoaderBuilder<ImageLoaderBuilder>() {

    override var httpClient: Lazy<HttpClient> = lazy { HttpClient(CIO) }
    override var requestDispatcher: CoroutineDispatcher = Dispatchers.IO

    actual fun build(): ImageLoader {
        val components = componentBuilder
            // Mappers
            .add(KtorUrlMapper())
            // Keyers
            .add(KtorUlKeyer())
            // Fetchers
            .add(KtorUrlFetcher.Factory(httpClient))
            .add(FileFetcher.Factory())
            // Decoders
            .add(GifDecoder.Factory())
            .add(ImageIODecoder.Factory())
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
