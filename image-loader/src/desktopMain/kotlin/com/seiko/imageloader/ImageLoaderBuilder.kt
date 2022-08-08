package com.seiko.imageloader

import androidx.compose.ui.unit.Density
import com.seiko.imageloader.component.decoder.GifDecoder
import com.seiko.imageloader.component.decoder.SkiaImageDecoder
import com.seiko.imageloader.component.decoder.SvgDecoder
import com.seiko.imageloader.component.fetcher.FileFetcher
import com.seiko.imageloader.component.fetcher.KtorUrlFetcher
import com.seiko.imageloader.component.keyer.KtorUlKeyer
import com.seiko.imageloader.component.mapper.KtorUrlMapper
import com.seiko.imageloader.request.Options
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp

actual class ImageLoaderBuilder : CommonImageLoaderBuilder<ImageLoaderBuilder>() {

    override var httpClient: Lazy<HttpClient> = lazy { HttpClient(OkHttp) }
    private var density: Density? = null

    fun density(density: Density) = apply {
        this.density = density
    }

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
