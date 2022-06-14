package com.seiko.imageloader

import com.seiko.imageloader.cache.disk.DiskCache
import com.seiko.imageloader.cache.memory.MemoryCache
import com.seiko.imageloader.cache.memory.MemoryCacheBuilder
import com.seiko.imageloader.component.ComponentRegistryBuilder
import com.seiko.imageloader.component.fetcher.KtorUrlFetcher
import com.seiko.imageloader.component.keyer.KtorUlKeyer
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
    private var memoryCache: Lazy<MemoryCache> = lazy { MemoryCacheBuilder().build() }
    private var diskCache: Lazy<DiskCache>? = null
    private var requestDispatcher: CoroutineDispatcher = Dispatchers.Default

    actual fun httpClient(initializer: () -> HttpClient) = apply {
        httpClient = lazy(initializer)
    }

    actual fun memoryCache(initializer: () -> MemoryCache) = apply {
        memoryCache = lazy(initializer)
    }

    actual fun diskCache(initializer: () -> DiskCache) = apply {
        diskCache = lazy(initializer)
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
            .add(KtorUlKeyer())
            // Fetchers
            .add(KtorUrlFetcher.Factory(httpClient))
            // .add(FileFetcher.Factory())
            // Decoders
            // .add(ImageIODecoder.Factory())
            .build()

        return RealImageLoader(
            components = components,
            options = options ?: Options(),
            interceptors = interceptors,
            requestDispatcher = requestDispatcher,
            memoryCache = memoryCache,
            diskCache = diskCache,
        )
    }
}
