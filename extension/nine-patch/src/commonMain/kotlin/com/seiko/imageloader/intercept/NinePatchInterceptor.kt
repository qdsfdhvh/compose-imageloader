package com.seiko.imageloader.intercept

import com.seiko.imageloader.asImageBitmap
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.model.ninePatchData
import com.seiko.imageloader.util.NinePatchPainter

// only support Bitmap
class NinePatchInterceptor : Interceptor {
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val request = chain.request
        val result = chain.proceed(request)
        if (result is ImageResult.Bitmap) {
            val centerSlice = request.ninePatchData ?: return result
            return ImageResult.Painter(
                painter = NinePatchPainter(
                    image = result.bitmap.asImageBitmap(),
                    ninePatchData = centerSlice,
                ),
            )
        }
        return result
    }
}
