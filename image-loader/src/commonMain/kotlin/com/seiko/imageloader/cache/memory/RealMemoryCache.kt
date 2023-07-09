package com.seiko.imageloader.cache.memory

internal class RealMemoryCache<K : Any, V : Any>(
    private val strongMemoryCache: StrongMemoryCache<K, V>,
    private val weakMemoryCache: WeakMemoryCache<K, V>,
) : MemoryCache<K, V> {

    override val size get() = strongMemoryCache.size

    override val maxSize get() = strongMemoryCache.maxSize

    override val keys get() = strongMemoryCache.keys + weakMemoryCache.keys

    override fun get(key: K): V? {
        return strongMemoryCache.get(key) ?: weakMemoryCache.get(key)
    }

    override fun set(key: K, value: V) {
        // Ensure that stored keys and values are immutable.
        strongMemoryCache.set(
            key = key,
            value = value,
        )
        // weakMemoryCache.set() is called by strongMemoryCache when
        // a value is evicted from the strong reference cache.
    }

    override fun remove(key: K): Boolean {
        // Do not short circuit. There is a regression test for this.
        val removedStrong = strongMemoryCache.remove(key)
        val removedWeak = weakMemoryCache.remove(key)
        return removedStrong || removedWeak
    }

    override fun clear() {
        strongMemoryCache.evictAll()
        weakMemoryCache.evictAll()
    }
}
