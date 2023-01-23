package com.seiko.imageloader.cache.memory

class MemoryCacheBuilder internal constructor() {

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

    fun build(): MemoryCache {
        val weakMemoryCache = if (weakReferencesEnabled) {
            RealWeakMemoryCache()
        } else {
            EmptyWeakMemoryCache
        }
        val strongMemoryCache = if (strongReferencesEnabled) {
            if (maxSizeBytes > 0) {
                RealStrongMemoryCache(maxSizeBytes, weakMemoryCache)
            } else {
                EmptyStrongMemoryCache(weakMemoryCache)
            }
        } else {
            EmptyStrongMemoryCache(weakMemoryCache)
        }
        return RealMemoryCache(strongMemoryCache, weakMemoryCache)
    }
}

fun MemoryCache(block: MemoryCacheBuilder.() -> Unit) =
    MemoryCacheBuilder().apply(block).build()
