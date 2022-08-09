package com.seiko.imageloader.cache.memory

actual class MemoryCacheBuilder : CommonMemoryCacheBuilder<MemoryCacheBuilder>() {

    override var maxSizePercent: Double = STANDARD_MEMORY_MULTIPLIER

    override fun calculateMemoryCacheSize(percent: Double): Int {
        val memoryClassBytes = Runtime.getRuntime().totalMemory()
        return (percent * memoryClassBytes).toInt()
    }
}
