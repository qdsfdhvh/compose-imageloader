package com.seiko.imageloader.cache.memory

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class StrongMemoryCacheTest {

    @Test
    fun can_retrieve_cached_value() {
        val weakCache = RealWeakMemoryCache<String, String>(valueHashProvider = { it.hashCode() })
        val strongCache = RealStrongMemoryCache(200, weakCache, valueSizeProvider = { 100 })

        val bitmap = "bitmap"
        strongCache.set("1", bitmap)
        assertEquals(bitmap, strongCache.get("1"))
    }

    @Test
    fun least_recently_used_value_is_evicted() {
        val weakCache = RealWeakMemoryCache<String, String>(valueHashProvider = { it.hashCode() })
        val strongCache = RealStrongMemoryCache(200, weakCache, valueSizeProvider = { 100 })

        val first = "bitmap"
        strongCache.set("1", first)

        val second = "bitmap"
        strongCache.set("2", second)

        val third = "bitmap"
        strongCache.set("3", third)

        assertNull(strongCache.get("1"))
        assertNotNull(weakCache.get("1"))
    }

    @Test
    fun value_can_be_removed() {
        val weakCache = RealWeakMemoryCache<String, String>(valueHashProvider = { it.hashCode() })
        val strongCache = RealStrongMemoryCache(200, weakCache, valueSizeProvider = { 100 })

        val bitmap = "bitmap"
        strongCache.set("1", bitmap)
        strongCache.remove("1")

        assertNull(strongCache.get("1"))
        assertNotNull(weakCache.get("1"))
    }
}
