package com.seiko.imageloader.intercept

import com.seiko.imageloader.request.ImageResult

class MappedInterceptor : Interceptor {
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val (request, options, components) = chain

        val data = request.data
        val mappedData = components.map(data, options)
        val newRequest = request.newBuilder().data(mappedData).build()
        return chain.proceed(newRequest)
    }
}
