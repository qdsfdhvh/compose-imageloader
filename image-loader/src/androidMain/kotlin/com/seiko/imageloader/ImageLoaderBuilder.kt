package com.seiko.imageloader

import android.content.Context
import com.seiko.imageloader.component.ComponentRegistryBuilder
import com.seiko.imageloader.component.decoder.BitmapFactoryDecoder
import com.seiko.imageloader.component.fetcher.AssetUriFetcher
import com.seiko.imageloader.component.fetcher.BitmapFetcher
import com.seiko.imageloader.component.fetcher.ByteBufferFetcher
import com.seiko.imageloader.component.fetcher.ContentUriFetcher
import com.seiko.imageloader.component.fetcher.DrawableFetcher
import com.seiko.imageloader.component.fetcher.FileFetcher
import com.seiko.imageloader.component.fetcher.KtorUrlFetcher
import com.seiko.imageloader.component.fetcher.ResourceUriFetcher
import com.seiko.imageloader.component.keyer.FileKeyer
import com.seiko.imageloader.component.keyer.UriKeyer
import com.seiko.imageloader.component.mapper.ByteArrayMapper
import com.seiko.imageloader.component.mapper.FileUriMapper
import com.seiko.imageloader.component.mapper.KtorUrlMapper
import com.seiko.imageloader.component.mapper.ResourceIntMapper
import com.seiko.imageloader.component.mapper.ResourceUriMapper
import com.seiko.imageloader.component.mapper.StringMapper
import com.seiko.imageloader.intercept.Interceptor
import com.seiko.imageloader.request.Options
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual class ImageLoaderBuilder constructor(context: Context) {

    private val context = context.applicationContext

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
            .add(StringMapper())
            .add(FileUriMapper())
            .add(ResourceUriMapper(context))
            .add(ResourceIntMapper(context))
            .add(ByteArrayMapper())
            // Keyers
            .add(FileKeyer(true))
            .add(UriKeyer(context))
            // Fetchers
            .add(KtorUrlFetcher.Factory(httpClient))
            .add(FileFetcher.Factory())
            .add(AssetUriFetcher.Factory(context))
            .add(ContentUriFetcher.Factory(context))
            .add(ResourceUriFetcher.Factory(context))
            .add(DrawableFetcher.Factory())
            .add(BitmapFetcher.Factory())
            .add(ByteBufferFetcher.Factory())
            // Decoders
            .add(BitmapFactoryDecoder.Factory(context))
            .build()

        return RealImageLoader(
            components = components,
            options = options ?: Options(),
            interceptors = interceptors,
            requestDispatcher = requestDispatcher,
        )
    }
}