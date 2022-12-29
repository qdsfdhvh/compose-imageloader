package com.seiko.imageloader.intercept

import com.seiko.imageloader.request.ImageResult

class MappedInterceptor : Interceptor {
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val mappedData = chain.components.map(chain.request.data, chain.options)
        val newRequest = chain.request.newBuilder().data(mappedData).build()
        return chain.proceed(newRequest)
    }
}
