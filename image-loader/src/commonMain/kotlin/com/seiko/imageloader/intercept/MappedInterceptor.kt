package com.seiko.imageloader.intercept

import com.seiko.imageloader.request.ImageResult

class MappedInterceptor : Interceptor {
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val request = chain.request
        val options = chain.options
        val components = chain.components

        val mappedData = components.map(request.data, options)
        val newRequest = request.newBuilder().data(mappedData).build()
        return chain.proceed(newRequest)
    }
}
