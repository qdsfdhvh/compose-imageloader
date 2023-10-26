package com.seiko.imageloader.screenshot

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.intercept.Interceptor
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.rememberImagePainter
import kotlinx.coroutines.delay

abstract class ComposeScreenShotCommonTest {

    @Composable
    protected fun TestLoadImageUI() {
        val url = "https://example.com/url.jpg"
        val imageLoader = remember {
            ImageLoader {
                interceptor {
                    useDefaultInterceptors = false
                    addInterceptor(
                        Interceptor { chain ->
                            val color = if (url == chain.request.data) Color.Green else Color.Blue
                            ImageResult.OfPainter(ColorPainter(color))
                        },
                    )
                }
            }
        }
        Row {
            Image(
                rememberImagePainter(url, imageLoader),
                contentDescription = "green",
                modifier = Modifier.size(100.dp),
            )
            Spacer(Modifier.width(8.dp))
            Image(
                rememberImagePainter("", imageLoader),
                contentDescription = "blue",
                modifier = Modifier.size(100.dp),
            )
        }
    }

    @Composable
    protected fun TestPlaceholderPainterUI() {
        val imageLoader = remember {
            ImageLoader {
                interceptor {
                    useDefaultInterceptors = false
                    addInterceptor {
                        delay(100)
                        ImageResult.OfPainter(ColorPainter(Color.Green))
                    }
                }
            }
        }
        Image(
            rememberImagePainter(
                "",
                imageLoader,
                placeholderPainter = { ColorPainter(Color.Gray) },
            ),
            contentDescription = "placeholder",
            modifier = Modifier.size(100.dp),
        )
    }

    @Composable
    protected fun TestErrorPainterUI() {
        val imageLoader = remember {
            ImageLoader {
                interceptor {
                    useDefaultInterceptors = false
                    addInterceptor {
                        ImageResult.OfError(RuntimeException("error"))
                    }
                }
            }
        }
        Image(
            rememberImagePainter(
                "",
                imageLoader,
                errorPainter = { ColorPainter(Color.Red) },
            ),
            contentDescription = "error",
            modifier = Modifier.size(100.dp),
        )
    }
}
