package com.seiko.imageloader

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import app.cash.turbine.test
import com.seiko.imageloader.component.fetcher.FetchResult
import com.seiko.imageloader.component.fetcher.Fetcher
import com.seiko.imageloader.model.ImageEvent
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.model.ImageResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ImageLoaderTest {

    private lateinit var imageLoader: ImageLoader

    private lateinit var resultPainter1: Painter
    private lateinit var resultPainter2: Painter
    private lateinit var resultPainter3: Painter

    @BeforeTest
    fun onBefore() {
        resultPainter1 = ColorPainter(Color.Green)
        resultPainter2 = ColorPainter(Color.Red)
        resultPainter3 = ColorPainter(Color.Blue)
        imageLoader = ImageLoader {
            components {
                add(
                    Fetcher.Factory { data, _ ->
                        Fetcher {
                            FetchResult.OfPainter(
                                when (data) {
                                    "1" -> resultPainter1
                                    "2" -> resultPainter2
                                    else -> resultPainter3
                                },
                            )
                        }
                    },
                )
            }
        }
    }

    @Test
    fun request_test() = runTest {
        val request = ImageRequest("1")
        imageLoader.async(request).test {
            assertEquals(ImageEvent.Start, awaitItem())
            assertEquals(ImageEvent.StartWithFetch, awaitItem())
            assertEquals(ImageResult.OfPainter(resultPainter1), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun request_state_test() = runTest {
        val requestFlow = flow {
            emit(ImageRequest("1"))
            emit(ImageRequest("2"))
            emit(ImageRequest("3") { skipEvent = true })
        }
        @OptIn(ExperimentalCoroutinesApi::class)
        requestFlow.flatMapLatest { imageLoader.async(it) }.test {
            // 1
            assertEquals(ImageEvent.Start, awaitItem())
            assertEquals(ImageEvent.StartWithFetch, awaitItem())
            assertEquals(ImageResult.OfPainter(resultPainter1), awaitItem())
            // 2
            assertEquals(ImageEvent.Start, awaitItem())
            assertEquals(ImageEvent.StartWithFetch, awaitItem())
            assertEquals(ImageResult.OfPainter(resultPainter2), awaitItem())
            // 3
            assertEquals(ImageResult.OfPainter(resultPainter3), awaitItem())
            awaitComplete()
        }
    }
}
