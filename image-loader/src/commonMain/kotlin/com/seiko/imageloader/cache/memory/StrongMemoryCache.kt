package com.seiko.imageloader.cache.memory

import com.seiko.imageloader.util.LruCache

/** An in-memory cache that holds strong references [V]s. */
internal interface StrongMemoryCache<K, V> {

    val size: Int

    val maxSize: Int

    val keys: Set<K>

    fun get(key: K): V?

    fun set(key: K, value: V)

    fun remove(key: K): Boolean

    fun evictAll()
}

/** A [StrongMemoryCache] implementation that caches nothing. */
internal open class EmptyStrongMemoryCache<K : Any, V : Any>(
    private val weakMemoryCache: WeakMemoryCache<K, V>,
    private val valueSizeProvider: (V) -> Int,
) : StrongMemoryCache<K, V> {

    override val size get() = 0

    override val maxSize get() = 0

    override val keys get() = emptySet<K>()

    override fun get(key: K): V? = null

    override fun set(key: K, value: V) {
        weakMemoryCache.set(key, value, valueSizeProvider(value))
    }

    override fun remove(key: K) = false
    override fun evictAll() = Unit
}

/** A [StrongMemoryCache] implementation backed by an [LruCache]. */
internal open class RealStrongMemoryCache<K : Any, V : Any>(
    maxSize: Int,
    private val weakMemoryCache: WeakMemoryCache<K, V>,
    private val valueSizeProvider: (V) -> Int,
) : StrongMemoryCache<K, V> {

    private val cache = object : LruCache<K, InternalValue<V>>(maxSize) {
        override fun sizeOf(key: K, value: InternalValue<V>) = value.size
        override fun entryRemoved(
            evicted: Boolean,
            key: K,
            oldValue: InternalValue<V>,
            newValue: InternalValue<V>?,
        ) = weakMemoryCache.set(key, oldValue.value, oldValue.size)
    }

    override val size get() = cache.size()

    override val maxSize get() = cache.maxSize()

    override val keys get() = cache.snapshot().keys

    override fun get(key: K): V? {
        return cache[key]?.value
    }

    override fun set(key: K, value: V) {
        val size = valueSizeProvider(value)
        if (size <= maxSize) {
            cache.put(key, InternalValue(value, size))
        } else {
            // If the bitmap is too big for the cache, don't attempt to store it as doing
            // so will cause the cache to be cleared. Instead, evict an existing element
            // with the same key if it exists and add the bitmap to the weak memory cache.
            cache.remove(key)
            weakMemoryCache.set(key, value, size)
        }
    }

    override fun remove(key: K): Boolean {
        return cache.remove(key) != null
    }

    override fun evictAll() {
        cache.evictAll()
    }

    private class InternalValue<V>(
        val value: V,
        val size: Int,
    )
}
