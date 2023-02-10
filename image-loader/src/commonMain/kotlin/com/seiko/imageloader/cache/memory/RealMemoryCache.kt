package com.seiko.imageloader.cache.memory

internal class RealMemoryCache(
    private val strongMemoryCache: StrongMemoryCache,
    private val weakMemoryCache: WeakMemoryCache,
) : MemoryCache {

    override val size get() = strongMemoryCache.size

    override val maxSize get() = strongMemoryCache.maxSize

    override val keys get() = strongMemoryCache.keys + weakMemoryCache.keys

    override fun get(key: MemoryKey): MemoryValue? {
        return strongMemoryCache.get(key) ?: weakMemoryCache.get(key)
    }

    override fun set(key: MemoryKey, value: MemoryValue) {
        // Ensure that stored keys and values are immutable.
        strongMemoryCache.set(
            key = key,
            image = value,
            extras = emptyMap(),
        )
        // weakMemoryCache.set() is called by strongMemoryCache when
        // a value is evicted from the strong reference cache.
    }

    override fun remove(key: MemoryKey): Boolean {
        // Do not short circuit. There is a regression test for this.
        val removedStrong = strongMemoryCache.remove(key)
        val removedWeak = weakMemoryCache.remove(key)
        return removedStrong || removedWeak
    }

    override fun clear() {
        strongMemoryCache.clearMemory()
        weakMemoryCache.clearMemory()
    }
}
