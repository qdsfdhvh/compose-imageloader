package com.seiko.imageloader.cache.memory

import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.core.content.ContextCompat

fun MemoryCacheBuilder.maxSizePercent(
    context: Context,
    percent: Double = context.defaultMemoryCacheSizePercent(),
) {
    require(percent in 0.0..1.0) { "size must be in the range [0.0, 1.0]." }
    val maxSizeBytes = context.calculateMemoryCacheSize(percent)
    maxSizeBytes(maxSizeBytes)
}

private fun Context.defaultMemoryCacheSizePercent(): Double {
    return runCatching {
        if (activityManager?.isLowRamDevice == true) {
            LOW_MEMORY_MULTIPLIER
        } else {
            STANDARD_MEMORY_MULTIPLIER
        }
    }.getOrDefault(STANDARD_MEMORY_MULTIPLIER)
}

private fun Context.calculateMemoryCacheSize(percent: Double): Int {
    val memoryClassMegabytes = runCatching {
        val activityManager = activityManager ?: return@runCatching null
        val isLargeHeap = (applicationInfo.flags and ApplicationInfo.FLAG_LARGE_HEAP) != 0
        if (isLargeHeap) activityManager.largeMemoryClass else activityManager.memoryClass
    }.getOrNull() ?: DEFAULT_MEMORY_CLASS_MEGABYTES
    return (percent * memoryClassMegabytes * 1024 * 1024).toInt()
}

private val Context.activityManager: ActivityManager?
    get() = ContextCompat.getSystemService(this, ActivityManager::class.java)

private const val STANDARD_MEMORY_MULTIPLIER = 0.2
private const val LOW_MEMORY_MULTIPLIER = 0.15
private const val DEFAULT_MEMORY_CLASS_MEGABYTES = 256
