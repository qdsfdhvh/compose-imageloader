package com.seiko.imageloader.cache.memory

class MemoryCacheBuilder<K : Any, V : Any> internal constructor(
    private val valueHashProvider: (V) -> Int,
    private val valueSizeProvider: (V) -> Int,
) {

    private var maxSize = 0
    private var strongReferencesEnabled = true
    private var weakReferencesEnabled = true

    fun maxSize(size: Int) {
        require(size >= 0) { "size must be >= 0." }
        maxSize = size
    }

    fun strongReferencesEnabled(enable: Boolean) {
        strongReferencesEnabled = enable
    }

    fun weakReferencesEnabled(enable: Boolean) {
        weakReferencesEnabled = enable
    }

    internal fun build(): MemoryCache<K, V> {
        val weakMemoryCache: WeakMemoryCache<K, V> =
            if (weakReferencesEnabled) {
                RealWeakMemoryCache(valueHashProvider)
            } else {
                EmptyWeakMemoryCache()
            }
        val strongMemoryCache: StrongMemoryCache<K, V> =
            if (strongReferencesEnabled && maxSize > 0) {
                RealStrongMemoryCache(maxSize, weakMemoryCache, valueSizeProvider)
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
    valueSizeProvider = valueSizeProvider,
).apply(block).build()
