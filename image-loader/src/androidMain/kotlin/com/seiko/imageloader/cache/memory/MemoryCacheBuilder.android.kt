package com.seiko.imageloader.cache.memory

import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.core.content.ContextCompat

fun MemoryCacheBuilder.maxSizePercent(context: Context, percent: Double) {
    require(percent in 0.0..1.0) { "size must be in the range [0.0, 1.0]." }
    val maxSizeBytes = runCatching {
        val activityManager = ContextCompat.getSystemService(context, ActivityManager::class.java)
            ?: return@runCatching 0
        val isLargeHeap = (context.applicationInfo.flags and ApplicationInfo.FLAG_LARGE_HEAP) != 0
        if (isLargeHeap) activityManager.largeMemoryClass else activityManager.memoryClass
    }.getOrDefault(DEFAULT_MEMORY_CLASS_MEGABYTES)
    maxSizeBytes(maxSizeBytes)
}

private const val DEFAULT_MEMORY_CLASS_MEGABYTES = 256
