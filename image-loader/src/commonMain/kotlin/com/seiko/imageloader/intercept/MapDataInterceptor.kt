package com.seiko.imageloader.intercept

import com.seiko.imageloader.request.ImageResult
import io.github.aakira.napier.Napier

class MapDataInterceptor : Interceptor {
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        Napier.d(tag = "Interceptor") { "intercept MapDataInterceptor" }

        val request = chain.request
        val options = chain.options
        val mappedData = chain.components.map(request.data, options)
        return chain.proceed(request.newBuilder().data(mappedData).build())
    }
}