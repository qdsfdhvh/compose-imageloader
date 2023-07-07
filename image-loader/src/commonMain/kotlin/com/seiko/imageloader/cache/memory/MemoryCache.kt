package com.seiko.imageloader.cache.memory

interface MemoryCache<K, V> {

    /** The current size of the cache in bytes. */
    val size: Int

    /** The maximum size of the cache in bytes. */
    val maxSize: Int

    /** The keys present in the cache. */
    val keys: Set<K>

    /** Get the [V] associated with [K]. */
    operator fun get(key: K): V?

    /** Set the [V] associated with [K]. */
    operator fun set(key: K, value: V)

    /**
     * Remove the [V] referenced by [K].
     *
     * @return 'true' if [K] was present in the cache. Else, return 'false'.
     */
    fun remove(key: K): Boolean

    /** Remove all values from the memory cache. */
    fun clear()
}
