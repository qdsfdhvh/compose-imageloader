package com.seiko.imageloader.intercept

import com.seiko.imageloader.request.ImageResult
import io.github.aakira.napier.Napier

class CacheInterceptor : Interceptor {
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        Napier.d(tag = "Interceptor") { "intercept CacheInterceptor" }
        return chain.proceed(chain.request)
    }
}