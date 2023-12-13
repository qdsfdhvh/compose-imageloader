package com.seiko.imageloader.demo.util

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import co.touchlab.kermit.Severity
import com.seiko.imageloader.ImageLoaderConfigBuilder
import com.seiko.imageloader.component.fetcher.ComposeResourceFetcher
import com.seiko.imageloader.intercept.BlurInterceptor
import com.seiko.imageloader.intercept.Interceptor
import com.seiko.imageloader.intercept.NinePatchInterceptor
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.model.NullRequestData
import com.seiko.imageloader.util.LogPriority
import com.seiko.imageloader.util.Logger
import co.touchlab.kermit.Logger as DebugLogger

fun ImageLoaderConfigBuilder.commonConfig() {
    logger = object : Logger {
        override fun log(
            priority: LogPriority,
            tag: String,
            data: Any?,
            throwable: Throwable?,
            message: String,
        ) {
            DebugLogger.log(
                severity = when (priority) {
                    LogPriority.VERBOSE -> Severity.Verbose
                    LogPriority.DEBUG -> Severity.Debug
                    LogPriority.INFO -> Severity.Info
                    LogPriority.WARN -> Severity.Warn
                    LogPriority.ERROR -> Severity.Error
                    LogPriority.ASSERT -> Severity.Assert
                },
                tag = tag,
                throwable = throwable,
                message = buildString {
                    if (data != null) {
                        append("[image data] ")
                        append(data.toString().take(100))
                        append('\n')
                    }
                    append("[message] ")
                    append(message)
                },
            )
        }

        override fun isLoggable(priority: LogPriority) = priority >= LogPriority.DEBUG
    }
    components {
        // add(MokoResourceFetcher.Factory())
        add(ComposeResourceFetcher.Factory())
    }
    interceptor {
        addInterceptor(NinePatchInterceptor())
        addInterceptor(BlurInterceptor())
    }
}

/**
 * return empty painter if request is null or empty
 */
object NullDataInterceptor : Interceptor {

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val data = chain.request.data
        if (data === NullRequestData || data is String && data.isEmpty()) {
            return ImageResult.OfPainter(
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
