package com.seiko.imageloader.cache.memory

import androidx.compose.ui.graphics.painter.Painter
import com.seiko.imageloader.util.LockObject
import com.seiko.imageloader.util.WeakReference
import com.seiko.imageloader.util.firstNotNullOfOrNullIndices
import com.seiko.imageloader.util.removeIfIndices
import com.seiko.imageloader.util.synchronized

/**
 * An in-memory cache that holds weak references to [Painter]s.
 *
 * Bitmaps are added to [WeakMemoryCache] when they're removed from [StrongMemoryCache].
 */
internal interface WeakMemoryCache<K, V> {
    val keys: Set<K>
    fun get(key: K): V?
    fun set(key: K, value: V, size: Int)
    fun remove(key: K): Boolean
    fun evictAll()
}

/** A [WeakMemoryCache] implementation that holds no references. */
internal open class EmptyWeakMemoryCache<K, V> : WeakMemoryCache<K, V> {
    override val keys get(): Set<Nothing> = emptySet()
    override fun get(key: K): V? = null
    override fun set(key: K, value: V, size: Int) = Unit
    override fun remove(key: K) = false
    override fun evictAll() = Unit
}

/** A [WeakMemoryCache] implementation backed by a [LinkedHashMap]. */
internal open class RealWeakMemoryCache<K : Any, V : Any>(
    private val valueHashProvider: (V) -> Int,
) : WeakMemoryCache<K, V> {

    internal val cache = LinkedHashMap<K, ArrayList<InternalValue<V>>>()
    private var operationsSinceCleanUp = 0

    private val syncObject = LockObject()

    override val keys: Set<K>
        get() = synchronized(syncObject) { cache.keys.toSet() }

    override fun get(key: K): V? = synchronized(syncObject) {
        val values = cache[key] ?: return null

        // Find the first bitmap that hasn't been collected.
        val value = values.firstNotNullOfOrNullIndices { value ->
            value.value.get()
        }

        cleanUpIfNecessary()
        return value
    }

    override fun set(key: K, value: V, size: Int) =
        synchronized(syncObject) {
            val cacheValues = cache.getOrPut(key) { arrayListOf() }

            // Insert the value into the list sorted descending by size.
            run {
                val identityHashCode = valueHashProvider(value)
                val newCacheValue = InternalValue(
                    identityHashCode = identityHashCode,
                    value = WeakReference(value),
                    size = size,
                )
                for (index in cacheValues.indices) {
                    val cacheValue = cacheValues[index]
                    if (size >= cacheValue.size) {
                        if (cacheValue.identityHashCode == identityHashCode &&
                            cacheValue.value.get() === value
                        ) {
                            cacheValues[index] = newCacheValue
                        } else {
                            cacheValues.add(index, newCacheValue)
                        }
                        return@run
                    }
                }
                cacheValues += newCacheValue
            }

            cleanUpIfNecessary()
        }

    override fun remove(key: K): Boolean = synchronized(syncObject) {
        return cache.remove(key) != null
    }

    override fun evictAll() = synchronized(syncObject) {
        operationsSinceCleanUp = 0
        cache.clear()
    }

    private fun cleanUpIfNecessary() {
        if (operationsSinceCleanUp++ >= CLEAN_UP_INTERVAL) {
            cleanUp()
        }
    }

    /** Remove any dereferenced bitmaps from the cache. */
    internal fun cleanUp() {
        operationsSinceCleanUp = 0

        // Remove all the values whose references have been collected.
        val iterator = cache.values.iterator()
        while (iterator.hasNext()) {
            val list = iterator.next()

            if (list.count() <= 1) {
                // Typically, the list will only contain 1 item. Handle this case in an optimal way here.
                if (list.firstOrNull()?.value?.get() == null) {
                    iterator.remove()
                }
            } else {
                // Iterate over the list of values and delete individual entries that have been collected.
                list.removeIfIndices { it.value.get() == null }

                if (list.isEmpty()) {
                    iterator.remove()
                }
            }
        }
    }

    internal class InternalValue<V : Any>(
        val identityHashCode: Int,
        val value: WeakReference<V>,
        val size: Int,
    )

    companion object {
        private const val CLEAN_UP_INTERVAL = 10
    }
}
