package com.seiko.imageloader.cache.memory

fun <K : Any, V : Any> MemoryCacheBuilder<K, V>.maxSizePercent(percent: Double = STANDARD_MEMORY_MULTIPLIER) {
    val memoryClassMegabytes = Runtime.getRuntime().totalMemory()
    val maxSizeBytes = (percent * memoryClassMegabytes * 1024 * 1024).toInt()
    maxSize(maxSizeBytes)
}

private const val STANDARD_MEMORY_MULTIPLIER = 0.2
