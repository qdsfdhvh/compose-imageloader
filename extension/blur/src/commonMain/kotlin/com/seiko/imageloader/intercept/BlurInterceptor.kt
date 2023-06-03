package com.seiko.imageloader.intercept

import com.seiko.imageloader.Bitmap
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.model.blurEffects

// blur only support Bitmap
class BlurInterceptor : Interceptor {
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val request = chain.request
        val result = chain.proceed(request)
        if (result is ImageResult.Bitmap) {
            val blurEffects = request.blurEffects ?: return result
            return result.copy(bitmap = blur(result.bitmap, blurEffects.radius))
        }
        return result
    }
}

internal expect fun blur(input: Bitmap, radius: Int): Bitmap
