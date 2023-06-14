package com.seiko.imageloader.cache.memory

import com.seiko.imageloader.Bitmap
import com.seiko.imageloader.size
import com.seiko.imageloader.util.LruCache

/** An in-memory cache that holds strong references [Bitmap]s. */
internal interface StrongMemoryCache {

    val size: Int

    val maxSize: Int

    val keys: Set<MemoryKey>

    fun get(key: MemoryKey): MemoryValue?

    fun set(key: MemoryKey, image: Bitmap, extras: Map<String, Any>)

    fun remove(key: MemoryKey): Boolean

    fun clearMemory()
}

/** A [StrongMemoryCache] implementation that caches nothing. */
internal class EmptyStrongMemoryCache(
    private val weakMemoryCache: WeakMemoryCache,
) : StrongMemoryCache {

    override val size get() = 0

    override val maxSize get() = 0

    override val keys get() = emptySet<MemoryKey>()

    override fun get(key: MemoryKey): MemoryValue? = null

    override fun set(key: MemoryKey, image: Bitmap, extras: Map<String, Any>) {
        weakMemoryCache.set(key, image, extras, image.size)
    }

    override fun remove(key: MemoryKey) = false

    override fun clearMemory() {}
}

/** A [StrongMemoryCache] implementation backed by an [LruCache]. */
internal class RealStrongMemoryCache(
    maxSize: Int,
    private val weakMemoryCache: WeakMemoryCache,
) : StrongMemoryCache {

    private val cache = object : LruCache<MemoryKey, InternalValue>(maxSize) {
        override fun sizeOf(key: MemoryKey, value: InternalValue) = value.size
        override fun entryRemoved(
            evicted: Boolean,
            key: MemoryKey,
            oldValue: InternalValue,
            newValue: InternalValue?,
        ) = weakMemoryCache.set(key, oldValue.image, oldValue.extras, oldValue.size)
    }

    override val size get() = cache.size()

    override val maxSize get() = cache.maxSize()

    override val keys get() = cache.snapshot().keys

    override fun get(key: MemoryKey): MemoryValue? {
        return cache[key]?.image
    }

    override fun set(key: MemoryKey, image: Bitmap, extras: Map<String, Any>) {
        val size = image.size
        if (size <= maxSize) {
            cache.put(key, InternalValue(image, extras, size))
        } else {
            // If the bitmap is too big for the cache, don't attempt to store it as doing
            // so will cause the cache to be cleared. Instead, evict an existing element
            // with the same key if it exists and add the bitmap to the weak memory cache.
            cache.remove(key)
            weakMemoryCache.set(key, image, extras, size)
        }
    }

    override fun remove(key: MemoryKey): Boolean {
        return cache.remove(key) != null
    }

    override fun clearMemory() {
        cache.evictAll()
    }

    private class InternalValue(
        val image: Bitmap,
        val extras: Map<String, Any>,
        val size: Int,
    )
}
