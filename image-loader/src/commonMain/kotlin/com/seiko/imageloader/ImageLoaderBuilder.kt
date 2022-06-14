package com.seiko.imageloader

import com.seiko.imageloader.component.ComponentRegistryBuilder
import com.seiko.imageloader.intercept.Interceptor
import com.seiko.imageloader.request.Options
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher

expect class ImageLoaderBuilder {
    fun httpClient(initializer: () -> HttpClient): ImageLoaderBuilder
    fun components(builder: ComponentRegistryBuilder.() -> Unit): ImageLoaderBuilder
    fun addInterceptor(interceptor: Interceptor): ImageLoaderBuilder
    fun options(options: Options): ImageLoaderBuilder
    fun requestDispatcher(dispatcher: CoroutineDispatcher): ImageLoaderBuilder
    fun build(): ImageLoader
}