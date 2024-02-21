package com.seiko.imageloader.option

import androidx.compose.ui.geometry.Size
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class SizeResolverTest {

    @Test
    fun `size_resolver_compare_test`() {
        val sizeResolver = SizeResolver(Size(100f, 100f))
        val sizeResolver2 = SizeResolver(Size(100f, 100f))
        assertEquals(sizeResolver, sizeResolver2)
    }

    @Test
    fun `size_resolver_not_compare_test`() {
        val sizeResolver = SizeResolver(Size(100f, 100f))
        val sizeResolver2 = SizeResolver(Size(10f, 10f))
        assertNotEquals(sizeResolver, sizeResolver2)
    }
}
