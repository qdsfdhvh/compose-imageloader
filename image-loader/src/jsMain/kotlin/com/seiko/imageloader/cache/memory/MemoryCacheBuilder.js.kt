package com.seiko.imageloader.cache.memory

actual class MemoryCacheBuilder : CommonMemoryCacheBuilder<MemoryCacheBuilder>() {

    override var maxSizePercent: Double = STANDARD_MEMORY_MULTIPLIER

    override fun calculateMemoryCacheSize(percent: Double): Int {
        val memoryClassMegabytes = DEFAULT_MEMORY_CLASS_MEGABYTES
        return (percent * memoryClassMegabytes * 1024 * 1024).toInt()
    }
}
