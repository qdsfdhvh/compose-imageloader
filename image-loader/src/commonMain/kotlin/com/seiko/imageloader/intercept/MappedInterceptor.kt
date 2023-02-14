package com.seiko.imageloader.intercept

import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.util.d

class MappedInterceptor : Interceptor {
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val request = chain.request
        val options = chain.options
        val logger = chain.config.logger

        val mappedData = chain.components.map(request.data, options)
        logger.d(
            tag = "MappedInterceptor",
            data = request.data,
        ) { "map -> $mappedData" }

        val newRequest = request.newBuilder { data(mappedData) }
        return chain.proceed(newRequest)
    }
}
