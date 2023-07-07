package com.seiko.imageloader.cache.memory

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class WeakMemoryCacheTest {
    private lateinit var weakMemoryCache: RealWeakMemoryCache<String, TestValue>
    private lateinit var references: MutableSet<Any>

    @BeforeTest
    fun before() {
        weakMemoryCache = RealWeakMemoryCache(valueHashProvider = { it.hashCode() })
        references = mutableSetOf()
    }

    @Test
    fun can_retrieve_cached_value() {
        val key = "key"
        val bitmap = reference(TestValue(100))
        val size = bitmap.size

        weakMemoryCache.set(key, bitmap, size)
        val value = weakMemoryCache.get(key)

        assertNotNull(value)
        assertEquals(bitmap, value)
    }

    @Test
    fun can_hold_multiple_values() {
        val bitmap1 = reference(TestValue(100))
        weakMemoryCache.set("key1", bitmap1, 100)

        val bitmap2 = reference(TestValue(100))
        weakMemoryCache.set("key2", bitmap2, 100)

        val bitmap3 = reference(TestValue(100))
        weakMemoryCache.set("key3", bitmap3, 100)

        assertEquals(bitmap1, weakMemoryCache.get("key1"))
        assertEquals(bitmap2, weakMemoryCache.get("key2"))
        assertEquals(bitmap3, weakMemoryCache.get("key3"))

        weakMemoryCache.clear(bitmap2)

        assertEquals(bitmap1, weakMemoryCache.get("key1"))
        assertNull(weakMemoryCache.get("key2"))
        assertEquals(bitmap3, weakMemoryCache.get("key3"))
    }

    @Test
    fun empty_references_are_removed_from_cache() {
        val key = "key"
        val bitmap = reference(TestValue(100))

        weakMemoryCache.set(key, bitmap, 100)
        weakMemoryCache.clear(bitmap)

        assertNull(weakMemoryCache.get(key))
    }

    @Test
    fun bitmaps_with_same_key_are_retrieved_by_size_descending() {
        val bitmap1 = reference(TestValue(100))
        val bitmap2 = reference(TestValue(100))
        val bitmap3 = reference(TestValue(100))
        val bitmap4 = reference(TestValue(100))
        val bitmap5 = reference(TestValue(100))
        val bitmap6 = reference(TestValue(100))
        val bitmap7 = reference(TestValue(100))
        val bitmap8 = reference(TestValue(100))

        weakMemoryCache.set("key", bitmap1, 1)
        weakMemoryCache.set("key", bitmap3, 3)
        weakMemoryCache.set("key", bitmap5, 5)
        weakMemoryCache.set("key", bitmap7, 7)
        weakMemoryCache.set("key", bitmap8, 8)
        weakMemoryCache.set("key", bitmap4, 4)
        weakMemoryCache.set("key", bitmap6, 6)
        weakMemoryCache.set("key", bitmap2, 2)

        assertEquals(bitmap8, weakMemoryCache.get("key"))
        weakMemoryCache.clear(bitmap8)

        assertEquals(bitmap7, weakMemoryCache.get("key"))
        weakMemoryCache.clear(bitmap7)

        assertEquals(bitmap6, weakMemoryCache.get("key"))
        weakMemoryCache.clear(bitmap6)

        assertEquals(bitmap5, weakMemoryCache.get("key"))
        weakMemoryCache.clear(bitmap5)

        assertEquals(bitmap4, weakMemoryCache.get("key"))
        weakMemoryCache.clear(bitmap4)

        assertEquals(bitmap3, weakMemoryCache.get("key"))
        weakMemoryCache.clear(bitmap3)

        assertEquals(bitmap2, weakMemoryCache.get("key"))
        weakMemoryCache.clear(bitmap2)

        assertEquals(bitmap1, weakMemoryCache.get("key"))
        weakMemoryCache.clear(bitmap1)

        // All the values are invalidated.
        assertNull(weakMemoryCache.get("key"))
    }

    @Test
    fun cleanUp_clears_all_collected_values() {
        val bitmap1 = reference(TestValue(100))
        weakMemoryCache.set("key1", bitmap1, 100)

        val bitmap2 = reference(TestValue(100))
        weakMemoryCache.set("key2", bitmap2, 100)

        val bitmap3 = reference(TestValue(100))
        weakMemoryCache.set("key3", bitmap3, 100)

        weakMemoryCache.clear(bitmap1)
        assertNull(weakMemoryCache.get("key1"))

        weakMemoryCache.clear(bitmap3)
        assertNull(weakMemoryCache.get("key3"))

        assertEquals(3, weakMemoryCache.keys.size)

        weakMemoryCache.cleanUp()

        assertEquals(1, weakMemoryCache.keys.size)

        assertNull(weakMemoryCache.get("key1"))
        assertEquals(bitmap2, weakMemoryCache.get("key2"))
        assertNull(weakMemoryCache.get("key3"))
    }

    @Test
    fun value_is_removed_after_invalidate_is_called() {
        val key = "1"
        val bitmap = TestValue(100)
        weakMemoryCache.set(key, bitmap, 100)
        weakMemoryCache.remove(key)

        assertNull(weakMemoryCache.get(key))
    }

    /**
     * Clears [bitmap]'s weak reference without removing its entry from
     * [RealWeakMemoryCache.cache]. This simulates garbage collection.
     */
    private fun RealWeakMemoryCache<String, TestValue>.clear(bitmap: TestValue) {
        cache.values.forEach { values ->
            values.forEach { value ->
                if (value.value.get() === bitmap) {
                    value.value.clear()
                    return
                }
            }
        }
    }

    /**
     * Hold a strong reference to the value for the duration of the test
     * to prevent it from being garbage collected.
     */
    private fun <T : Any> reference(value: T): T {
        references += value
        return value
    }

    private class TestValue(val size: Int)
}
