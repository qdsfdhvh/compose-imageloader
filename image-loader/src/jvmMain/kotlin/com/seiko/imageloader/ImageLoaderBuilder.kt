package com.seiko.imageloader

import com.seiko.imageloader.component.ComponentRegistryBuilder
import com.seiko.imageloader.component.decoder.ImageIODecoder
import com.seiko.imageloader.component.fetcher.KtorUrlFetcher
import com.seiko.imageloader.component.mapper.KtorUrlMapper
import com.seiko.imageloader.intercept.Interceptor
import com.seiko.imageloader.request.Options
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual class ImageLoaderBuilder {

    private val componentBuilder = ComponentRegistryBuilder()
    private val interceptors = mutableListOf<Interceptor>()
    private var options: Options? = null

    private var httpClient: Lazy<HttpClient> = lazy { HttpClient(CIO) }
    private var requestDispatcher: CoroutineDispatcher = Dispatchers.IO

    actual fun httpClient(initializer: () -> HttpClient) = apply {
        httpClient = lazy(initializer)
    }

    actual fun components(builder: ComponentRegistryBuilder.() -> Unit) = apply {
        componentBuilder.run(builder)
    }

    actual fun addInterceptor(interceptor: Interceptor) = apply {
        interceptors.add(interceptor)
    }

    actual fun options(options: Options) = apply {
        this.options = options
    }

    actual fun requestDispatcher(dispatcher: CoroutineDispatcher) = apply {
        this.requestDispatcher = dispatcher
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
            options = options ?: Options(),
            interceptors = interceptors,
            requestDispatcher = requestDispatcher,
        )
    }
}