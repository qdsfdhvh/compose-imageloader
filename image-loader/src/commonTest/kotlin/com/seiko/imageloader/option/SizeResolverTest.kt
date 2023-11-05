package com.seiko.imageloader.option

import androidx.compose.ui.geometry.Size
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SizeResolverTest {

    @Test
    fun compare_test() {
        assertEquals(
            SizeResolver(Size(10f, 10f)),
            SizeResolver(Size(10.0f, 10.0f)),
        )
        assertEquals(
            SizeResolver(Size.Unspecified),
            SizeResolver.Unspecified,
        )
    }

    @Test
    fun async_size_test() = runTest {
        val sizeResolver = AsyncSizeResolver()
        launch {
            sizeResolver.setSize(Size(10f, 10f))
        }
        assertEquals(Size(10f, 10f), sizeResolver.size())
    }
}
