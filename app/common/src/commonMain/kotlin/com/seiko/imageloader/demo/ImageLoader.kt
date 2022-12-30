package com.seiko.imageloader.demo

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import com.seiko.imageloader.ImageLoaderBuilder
import com.seiko.imageloader.intercept.Interceptor
import com.seiko.imageloader.request.ComposePainterResult
import com.seiko.imageloader.request.ImageResult
import com.seiko.imageloader.request.NullRequestData
import com.seiko.imageloader.util.DebugLogger
import com.seiko.imageloader.util.LogPriority

fun ImageLoaderBuilder.commonConfig(): ImageLoaderBuilder {
    return logger(DebugLogger(LogPriority.VERBOSE)).addInterceptor(NullDataInterceptor)
}

/**
 * return empty painter if request is null or empty
 */
object NullDataInterceptor : Interceptor {

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val data = chain.request.data
        if (data === NullRequestData || data is String && data.isEmpty()) {
            return ComposePainterResult(
                request = chain.request,
                painter = EmptyPainter,
            )
        }
        return chain.proceed(chain.request)
    }

    private object EmptyPainter : Painter() {
        override val intrinsicSize: Size get() = Size.Unspecified
        override fun DrawScope.onDraw() {}
    }
}
