package com.seiko.imageloader.cache.memory

import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized

internal class RealMemoryCache<K : Any, V : Any>(
    private val strongMemoryCache: StrongMemoryCache<K, V>,
    private val weakMemoryCache: WeakMemoryCache<K, V>,
) : MemoryCache<K, V> {

    private val lock = SynchronizedObject()

    override val size: Int
        get() = synchronized(lock) { strongMemoryCache.size }

    override val maxSize: Int
        get() = synchronized(lock) { strongMemoryCache.maxSize }

    override val keys: Set<K>
        get() = synchronized(lock) { strongMemoryCache.keys + weakMemoryCache.keys }

    override fun get(key: K): V? = synchronized(lock) {
        val value = strongMemoryCache.get(key) ?: weakMemoryCache.get(key)

        // Remove unshareable images from the cache when they're returned.
        // if (value != null && !value.image.shareable) {
        //     remove(key)
        // }

        return value
    }

    override fun set(key: K, value: V) = synchronized(lock) {
        // Ensure that stored keys and values are immutable.
        strongMemoryCache.set(
            key = key,
            value = value,
        )
        // weakMemoryCache.set() is called by strongMemoryCache when
        // a value is evicted from the strong reference cache.
    }

    override fun remove(key: K): Boolean = synchronized(lock) {
        // Do not short circuit. There is a regression test for this.
        val removedStrong = strongMemoryCache.remove(key)
        val removedWeak = weakMemoryCache.remove(key)
        return removedStrong || removedWeak
    }

    override fun clear() = synchronized(lock) {
        strongMemoryCache.evictAll()
        weakMemoryCache.evictAll()
    }
}
