package com.seiko.imageloader.cache.memory

import com.seiko.imageloader.Bitmap
import com.seiko.imageloader.identityHashCode
import com.seiko.imageloader.size

class MemoryCacheBuilder<K : Any, V : Any> internal constructor(
    private val valueHashProvider: (V) -> Int,
    private val valueSizeProvider: (V) -> Int,
) {

    private var maxSizeBytes = 0
    private var strongReferencesEnabled = true
    private var weakReferencesEnabled = true

    fun maxSizeBytes(size: Int) {
        require(size >= 0) { "size must be >= 0." }
        maxSizeBytes = size
    }

    fun strongReferencesEnabled(enable: Boolean) {
        strongReferencesEnabled = enable
    }

    fun weakReferencesEnabled(enable: Boolean) {
        weakReferencesEnabled = enable
    }

    internal fun build(): MemoryCache<K, V> {
        val weakMemoryCache: WeakMemoryCache<K, V> = if (weakReferencesEnabled) {
            RealWeakMemoryCache(valueHashProvider)
        } else {
            EmptyWeakMemoryCache()
        }
        val strongMemoryCache: StrongMemoryCache<K, V> =
            if (strongReferencesEnabled && maxSizeBytes > 0) {
                RealStrongMemoryCache(maxSizeBytes, weakMemoryCache, valueSizeProvider)
            } else {
                EmptyStrongMemoryCache(weakMemoryCache, valueSizeProvider)
            }
        return RealMemoryCache(strongMemoryCache, weakMemoryCache)
    }
}

fun <K : Any, V : Any> MemoryCache(
    valueHashProvider: (V) -> Int,
    valueSizeProvider: (V) -> Int,
    block: MemoryCacheBuilder<K, V>.() -> Unit,
) = MemoryCacheBuilder<K, V>(
    valueHashProvider = valueHashProvider,
    valueSizeProvider = valueSizeProvider
).apply(block).build()

typealias MemoryKey = String

typealias MemoryValue = Bitmap
