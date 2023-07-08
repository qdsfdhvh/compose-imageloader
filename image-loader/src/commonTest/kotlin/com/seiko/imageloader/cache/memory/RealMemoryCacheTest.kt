package com.seiko.imageloader.cache.memory

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class RealMemoryCacheTest {

    private lateinit var weakCache: RealWeakMemoryCache<String, String>
    private lateinit var strongCache: RealStrongMemoryCache<String, String>
    private lateinit var cache: MemoryCache<String, String>

    private val valueSizeProvider: (String) -> Int = { ValueSize }

    @BeforeTest
    fun before() {
        weakCache = RealWeakMemoryCache(valueHashProvider = { it.hashCode() })
        strongCache = RealStrongMemoryCache(Int.MAX_VALUE, weakCache, valueSizeProvider)
        cache = RealMemoryCache(strongCache, weakCache)
    }

    @Test
    fun can_retrieve_strong_cached_value() {
        val key = "strong"
        val bitmap = "bitmap"
        assertNull(cache[key])
        strongCache.set(key, bitmap)
        assertEquals(bitmap, cache[key])
    }

    @Test
    fun can_retrieve_weak_cached_value() {
        val key = "weak"
        val bitmap = "bitmap"
        assertNull(cache[key])
        weakCache.set(key, bitmap, valueSizeProvider(bitmap))
        assertEquals(bitmap, cache[key])
    }

    @Test
    fun remove_removes_from_both_caches() {
        val key = "key"
        val bitmap = "bitmap"
        assertNull(cache[key])
        strongCache.set(key, bitmap)
        weakCache.set(key, bitmap, valueSizeProvider(bitmap))
        assertTrue(cache.remove(key))
        assertNull(strongCache.get(key))
        assertNull(weakCache.get(key))
    }

    @Test
    fun clear_clears_all_values() {
        assertEquals(0, cache.size)
        strongCache.set("a", "bitmap")
        strongCache.set("b", "bitmap")
        weakCache.set("c", "bitmap", ValueSize)
        weakCache.set("d", "bitmap", ValueSize)
        assertEquals(ValueSize * 2, cache.size)
        cache.clear()
        assertEquals(0, cache.size)
        assertNull(cache["a"])
        assertNull(cache["b"])
        assertNull(cache["c"])
        assertNull(cache["d"])
    }

    @Test
    fun set_can_be_retrieved_with_get() {
        val key = "a"
        val bitmap = "bitmap"
        cache[key] = bitmap
        assertEquals(bitmap, cache[key])
    }

    @Test
    fun setting_the_same_bitmap_multiple_times_can_only_be_removed_once() {
        val key = "a"
        val bitmap = "bitmap"
        weakCache.set(key, bitmap, ValueSize)
        weakCache.set(key, bitmap, ValueSize)
        assertTrue(weakCache.remove(key))
        assertFalse(weakCache.remove(key))
    }

    companion object {
        private const val ValueSize = 100
    }
}
