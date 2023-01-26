package com.seiko.imageloader.cache.memory

fun MemoryCacheBuilder.maxSizePercent(percent: Double) {
    val memoryClassMegabytes = Runtime.getRuntime().totalMemory()
    val maxSizeBytes = (percent * memoryClassMegabytes * 1024 * 1024).toInt()
    maxSizeBytes(maxSizeBytes)
}
