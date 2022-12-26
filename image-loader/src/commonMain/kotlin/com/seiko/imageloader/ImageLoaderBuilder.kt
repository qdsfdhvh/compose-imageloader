package com.seiko.imageloader

import com.seiko.imageloader.cache.disk.DiskCache
import com.seiko.imageloader.cache.memory.MemoryCache
import com.seiko.imageloader.component.ComponentRegistryBuilder
import com.seiko.imageloader.intercept.Interceptor
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.util.Logger
import com.seiko.imageloader.util.httpEngine
import com.seiko.imageloader.util.ioDispatcher
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

expect class ImageLoaderBuilder : CommonImageLoaderBuilder<ImageLoaderBuilder> {
    fun build(): ImageLoader
}

abstract class CommonImageLoaderBuilder<B : CommonImageLoaderBuilder<B>> {

    protected val componentBuilder = ComponentRegistryBuilder()
    protected val interceptors = mutableListOf<Interceptor>()
    protected var options: Options? = null

    protected var httpClient: Lazy<HttpClient> = lazy { HttpClient(httpEngine) }

    protected var memoryCache: Lazy<MemoryCache>? = null
    protected var diskCache: Lazy<DiskCache>? = null
    protected var requestDispatcher: CoroutineDispatcher = ioDispatcher
    protected var imageScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private inline fun apply(block: () -> Unit): B {
        block()
        @Suppress("UNCHECKED_CAST")
        return this as B
    }

    fun httpClient(initializer: () -> HttpClient) = apply {
        httpClient = lazy(initializer)
    }

    fun memoryCache(initializer: (() -> MemoryCache)?) = apply {
        memoryCache = initializer?.let { lazy(it) }
    }

    fun diskCache(initializer: (() -> DiskCache)?) = apply {
        diskCache = initializer?.let { lazy(it) }
    }

    fun components(builder: ComponentRegistryBuilder.() -> Unit) = apply {
        componentBuilder.run(builder)
    }

    fun addInterceptor(interceptor: Interceptor) = apply {
        interceptors.add(interceptor)
    }

    fun logger(vararg logger: Logger) = apply {
        Logger.base(*logger)
    }

    fun options(options: Options) = apply {
        this.options = options
    }

    fun requestDispatcher(dispatcher: CoroutineDispatcher) = apply {
        this.requestDispatcher = dispatcher
    }

    fun imageScope(scope: CoroutineScope) = apply {
        this.imageScope = scope
    }
}
