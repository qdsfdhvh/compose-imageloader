package com.seiko.imageloader.cache.memory

fun MemoryCacheBuilder.maxSizePercent(percent: Double = STANDARD_MEMORY_MULTIPLIER) {
    val memoryClassMegabytes = DEFAULT_MEMORY_CLASS_MEGABYTES
    val maxSizeBytes = (percent * memoryClassMegabytes * 1024 * 1024).toInt()
    maxSizeBytes(maxSizeBytes)
}

private const val STANDARD_MEMORY_MULTIPLIER = 0.2
private const val DEFAULT_MEMORY_CLASS_MEGABYTES = 256
