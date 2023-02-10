package com.seiko.imageloader.cache.memory

import androidx.compose.ui.graphics.painter.Painter
import com.seiko.imageloader.Bitmap
import com.seiko.imageloader.identityHashCode
import com.seiko.imageloader.util.WeakReference
import com.seiko.imageloader.util.firstNotNullOfOrNullIndices
import com.seiko.imageloader.util.removeIfIndices
import kotlin.jvm.Synchronized

/**
 * An in-memory cache that holds weak references to [Painter]s.
 *
 * Bitmaps are added to [WeakMemoryCache] when they're removed from [StrongMemoryCache].
 */
internal interface WeakMemoryCache {
    val keys: Set<MemoryKey>
    fun get(key: MemoryKey): MemoryValue?
    fun set(key: MemoryKey, image: Bitmap, extras: Map<String, Any>, size: Int)
    fun remove(key: MemoryKey): Boolean
    fun clearMemory()
}

/** A [WeakMemoryCache] implementation that holds no references. */
internal object EmptyWeakMemoryCache : WeakMemoryCache {
    override val keys get(): Set<MemoryKey> = emptySet()
    override fun get(key: MemoryKey): MemoryValue? = null
    override fun set(key: MemoryKey, image: Bitmap, extras: Map<String, Any>, size: Int) = Unit
    override fun remove(key: MemoryKey) = false
    override fun clearMemory() = Unit
}

/** A [WeakMemoryCache] implementation backed by a [LinkedHashMap]. */
internal class RealWeakMemoryCache : WeakMemoryCache {

    internal val cache = LinkedHashMap<MemoryKey, ArrayList<InternalValue>>()
    private var operationsSinceCleanUp = 0

    override val keys: Set<MemoryKey>
        @Synchronized
        get() = cache.keys.toSet()

    @Synchronized
    override fun get(key: MemoryKey): MemoryValue? {
        val values = cache[key] ?: return null

        // Find the first bitmap that hasn't been collected.
        val value = values.firstNotNullOfOrNullIndices { value ->
            value.image.get()
        }

        cleanUpIfNecessary()
        return value
    }

    @Synchronized
    override fun set(key: MemoryKey, image: Bitmap, extras: Map<String, Any>, size: Int) {
        val values = cache.getOrPut(key) { arrayListOf() }

        // Insert the value into the list sorted descending by size.
        run {
            val identityHashCode = image.identityHashCode
            val newValue = InternalValue(identityHashCode, WeakReference(image), extras, size)
            for (index in values.indices) {
                val value = values[index]
                if (size >= value.size) {
                    if (value.identityHashCode == identityHashCode && value.image.get() === image) {
                        values[index] = newValue
                    } else {
                        values.add(index, newValue)
                    }
                    return@run
                }
            }
            values += newValue
        }

        cleanUpIfNecessary()
    }

    @Synchronized
    override fun remove(key: MemoryKey): Boolean {
        return cache.remove(key) != null
    }

    @Synchronized
    override fun clearMemory() {
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
                if (list.firstOrNull()?.image?.get() == null) {
                    iterator.remove()
                }
            } else {
                // Iterate over the list of values and delete individual entries that have been collected.
                list.removeIfIndices { it.image.get() == null }

                if (list.isEmpty()) {
                    iterator.remove()
                }
            }
        }
    }

    internal class InternalValue(
        val identityHashCode: Int,
        val image: WeakReference<Bitmap>,
        val extras: Map<String, Any>,
        val size: Int,
    )

    companion object {
        private const val CLEAN_UP_INTERVAL = 10
    }
}
