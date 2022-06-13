package com.seiko.imageloader

import com.seiko.imageloader.component.ComponentRegistryBuilder
import com.seiko.imageloader.intercept.Interceptor
import io.ktor.client.HttpClient

expect class ImageLoaderBuilder {
    fun httpClient(initializer: () -> HttpClient): ImageLoaderBuilder
    fun components(builder: ComponentRegistryBuilder.() -> Unit): ImageLoaderBuilder
    fun addInterceptor(interceptor: Interceptor): ImageLoaderBuilder
    fun build(): ImageLoader
}