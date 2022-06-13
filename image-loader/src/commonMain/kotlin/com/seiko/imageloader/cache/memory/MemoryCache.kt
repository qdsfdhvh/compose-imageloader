package com.seiko.imageloader.cache.memory

interface MemoryCache {

    /** The current size of the cache in bytes. */
    val size: Int

    /** The maximum size of the cache in bytes. */
    val maxSize: Int

    /** The keys present in the cache. */
    val keys: Set<Key>

    /** Get the [Value] associated with [key]. */
    operator fun get(key: Key): Value?

    /** Set the [Value] associated with [key]. */
    operator fun set(key: Key, value: Value)

    /**
     * Remove the [Value] referenced by [key].
     *
     * @return 'true' if [key] was present in the cache. Else, return 'false'.
     */
    fun remove(key: Key): Boolean

    /** Remove all values from the memory cache. */
    fun clear()

    @JvmInline
    value class Key(val key: String)

    @JvmInline
    value class Value(val byteArray: ByteArray)
}
