package com.seiko.imageloader.cache.memory

import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.core.content.ContextCompat

actual class MemoryCacheBuilder(
    context: Context,
) : CommonMemoryCacheBuilder<MemoryCacheBuilder>() {

    private val context = context.applicationContext

    override var maxSizePercent: Double = defaultMemoryCacheSizePercent()

    private fun defaultMemoryCacheSizePercent(): Double {
        return runCatching {
            val activityManager: ActivityManager =
                ContextCompat.getSystemService(context, ActivityManager::class.java)!!
            if (activityManager.isLowRamDevice) LOW_MEMORY_MULTIPLIER else STANDARD_MEMORY_MULTIPLIER
        }.getOrDefault(STANDARD_MEMORY_MULTIPLIER)
    }

    override fun calculateMemoryCacheSize(percent: Double): Int {
        val memoryClassMegabytes = runCatching {
            val activityManager: ActivityManager =
                ContextCompat.getSystemService(context, ActivityManager::class.java)!!
            val isLargeHeap =
                (context.applicationInfo.flags and ApplicationInfo.FLAG_LARGE_HEAP) != 0
            if (isLargeHeap) activityManager.largeMemoryClass else activityManager.memoryClass
        }.getOrDefault(DEFAULT_MEMORY_CLASS_MEGABYTES)
        return (percent * memoryClassMegabytes * 1024 * 1024).toInt()
    }
}
