package com.seiko.imageloader.intercept

import com.seiko.imageloader.model.ImageResult

fun InterceptorsBuilder.addInterceptor(block: suspend (chain: Interceptor.Chain) -> ImageResult) {
    addInterceptor(Interceptor(block))
}
