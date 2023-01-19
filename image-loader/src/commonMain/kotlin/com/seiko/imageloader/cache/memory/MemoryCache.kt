package com.seiko.imageloader.cache.memory

import com.seiko.imageloader.Bitmap

interface MemoryCache {

    /** The current size of the cache in bytes. */
    val size: Int

    /** The maximum size of the cache in bytes. */
    val maxSize: Int

    /** The keys present in the cache. */
    val keys: Set<MemoryKey>

    /** Get the [MemoryValue] associated with [MemoryKey]. */
    operator fun get(key: MemoryKey): MemoryValue?

    /** Set the [MemoryValue] associated with [MemoryKey]. */
    operator fun set(key: MemoryKey, value: MemoryValue)

    /**
     * Remove the [MemoryValue] referenced by [MemoryKey].
     *
     * @return 'true' if [MemoryKey] was present in the cache. Else, return 'false'.
     */
    fun remove(key: MemoryKey): Boolean

    /** Remove all values from the memory cache. */
    fun clear()
}

typealias MemoryKey = String

typealias MemoryValue = Bitmap
