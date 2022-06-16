package com.seiko.imageloader

import com.seiko.imageloader.cache.disk.DiskCache
import com.seiko.imageloader.cache.memory.MemoryCache
import com.seiko.imageloader.cache.memory.MemoryCacheBuilder
import com.seiko.imageloader.component.ComponentRegistryBuilder
import com.seiko.imageloader.intercept.Interceptor
import com.seiko.imageloader.request.Options
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher

expect class ImageLoaderBuilder : CommonImageLoaderBuilder<ImageLoaderBuilder> {
    fun build(): ImageLoader
}

abstract class CommonImageLoaderBuilder<B : CommonImageLoaderBuilder<B>> {

    protected val componentBuilder = ComponentRegistryBuilder()
    protected val interceptors = mutableListOf<Interceptor>()
    protected var options: Options? = null

    protected abstract var httpClient: Lazy<HttpClient>
    protected var memoryCache: Lazy<MemoryCache> = lazy { MemoryCacheBuilder().build() }
    protected var diskCache: Lazy<DiskCache>? = null
    protected abstract var requestDispatcher: CoroutineDispatcher

    private inline fun apply(block: () -> Unit): B {
        block()
        @Suppress("UNCHECKED_CAST")
        return this as B
    }

    fun httpClient(initializer: () -> HttpClient) = apply {
        httpClient = lazy(initializer)
    }

    fun memoryCache(initializer: () -> MemoryCache) = apply {
        memoryCache = lazy(initializer)
    }

    fun diskCache(initializer: () -> DiskCache) = apply {
        diskCache = lazy(initializer)
    }

    fun components(builder: ComponentRegistryBuilder.() -> Unit) = apply {
        componentBuilder.run(builder)
    }

    fun addInterceptor(interceptor: Interceptor) = apply {
        interceptors.add(interceptor)
    }

    fun options(options: Options) = apply {
        this.options = options
    }

    fun requestDispatcher(dispatcher: CoroutineDispatcher) = apply {
        this.requestDispatcher = dispatcher
    }
}
