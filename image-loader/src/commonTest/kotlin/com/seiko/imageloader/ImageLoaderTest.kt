package com.seiko.imageloader

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import com.seiko.imageloader.component.fetcher.FetchResult
import com.seiko.imageloader.component.fetcher.Fetcher
import com.seiko.imageloader.model.ImageEvent
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.model.ImageResult
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.transform
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
        val list = imageLoader.async(request).toList()

        assertEquals(ImageEvent.Start, list[0])
        assertEquals(ImageEvent.StartWithFetch, list[1])
        assertEquals(ImageResult.OfPainter(resultPainter1), list[2])
    }

    @Test
    fun request_state_test() = runTest {
        val requestFlow = flow {
            emit(ImageRequest("1"))
            emit(ImageRequest("2"))
            emit(ImageRequest("3") { skipEvent = true })
        }
        val list = requestFlow.transform { emitAll(imageLoader.async(it)) }.toList()

        // 1
        assertEquals(ImageEvent.Start, list[0])
        assertEquals(ImageEvent.StartWithFetch, list[1])
        assertEquals(ImageResult.OfPainter(resultPainter1), list[2])
        // 2
        assertEquals(ImageEvent.Start, list[3])
        assertEquals(ImageEvent.StartWithFetch, list[4])
        assertEquals(ImageResult.OfPainter(resultPainter2), list[5])
        // 3
        assertEquals(ImageResult.OfPainter(resultPainter3), list[6])
    }
}
