package com.seiko.imageloader

import com.seiko.imageloader.component.ComponentRegistryBuilder
import com.seiko.imageloader.component.decoder.ImageIODecoder
import com.seiko.imageloader.component.fetcher.KtorUrlFetcher
import com.seiko.imageloader.component.mapper.KtorUrlMapper
import com.seiko.imageloader.intercept.Interceptor
import io.ktor.client.HttpClient

actual class ImageLoaderBuilder {

    private val componentBuilder = ComponentRegistryBuilder()
    private val interceptors = mutableListOf<Interceptor>()

    private var httpClient: Lazy<HttpClient> = lazy { HttpClient() }

    actual fun httpClient(initializer: () -> HttpClient) = apply {
        httpClient = lazy(initializer)
    }

    actual fun components(builder: ComponentRegistryBuilder.() -> Unit) = apply {
        componentBuilder.run(builder)
    }

    actual fun addInterceptor(interceptor: Interceptor) = apply {
        interceptors.add(interceptor)
    }

    actual fun build(): ImageLoader {
        val components = componentBuilder
            // Mappers
            .add(KtorUrlMapper())
            // Keyers
            // ...
            // Fetchers
            .add(KtorUrlFetcher.Factory(httpClient))
            // Decoders
            .add(ImageIODecoder.Factory())
            .build()

        return RealImageLoader(
            components = components,
            interceptors = interceptors,
        )
    }
}