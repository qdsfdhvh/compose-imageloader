package com.seiko.imageloader.intercept

import androidx.compose.ui.graphics.FilterQuality
import com.seiko.imageloader.asImageBitmap
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.model.ninePatchCenterRect
import com.seiko.imageloader.model.ninePatchFilterQuality
import com.seiko.imageloader.model.ninePatchScale
import com.seiko.imageloader.util.NinePatchPainter

// only support Bitmap
class NinePatchInterceptor : Interceptor {
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val request = chain.request
        val result = chain.proceed(request)
        if (result is ImageResult.Bitmap) {
            val centerSlice = request.ninePatchCenterRect ?: return result
            val scale = request.ninePatchScale ?: 1f
            val filterQuality = request.ninePatchFilterQuality ?: FilterQuality.Medium
            return ImageResult.Painter(
                painter = NinePatchPainter(
                    image = result.bitmap.asImageBitmap(),
                    centerSlice = centerSlice,
                    scale = scale,
                    filterQuality = filterQuality,
                ),
            )
        }
        return result
    }
}
