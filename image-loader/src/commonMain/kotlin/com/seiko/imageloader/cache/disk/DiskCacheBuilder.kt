package com.seiko.imageloader.cache.disk

import com.seiko.imageloader.util.directorySize
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okio.FileSystem
import okio.Path

class DiskCacheBuilder {

    private var directory: Path? = null
    private var fileSystem = FileSystem.SYSTEM
    private var maxSizePercent = 0.02 // 2%
    private var minimumMaxSizeBytes = 10L * 1024 * 1024 // 10MB
    private var maximumMaxSizeBytes = 250L * 1024 * 1024 // 250MB
    private var maxSizeBytes = 0L
    private var cleanupDispatcher = Dispatchers.IO

    /**
     * Set the [directory] where the cache stores its data.
     *
     * IMPORTANT: It is an error to have two [DiskCache] instances active in the same
     * directory at the same time as this can corrupt the disk cache.
     */
    fun directory(directory: Path) = apply {
        this.directory = directory
    }

    /**
     * Set the [fileSystem] where the cache stores its data, usually [FileSystem.SYSTEM].
     */
    fun fileSystem(fileSystem: FileSystem) = apply {
        this.fileSystem = fileSystem
    }

    /**
     * Set the maximum size of the disk cache as a percentage of the device's free disk space.
     */
    fun maxSizePercent(percent: Double) = apply {
        require(percent in 0.0..1.0) { "size must be in the range [0.0, 1.0]." }
        this.maxSizeBytes = 0
        this.maxSizePercent = percent
    }

    /**
     * Set the minimum size of the disk cache in bytes.
     * This is ignored if [maxSizeBytes] is set.
     */
    fun minimumMaxSizeBytes(size: Long) = apply {
        require(size > 0) { "size must be > 0." }
        this.minimumMaxSizeBytes = size
    }

    /**
     * Set the maximum size of the disk cache in bytes.
     * This is ignored if [maxSizeBytes] is set.
     */
    fun maximumMaxSizeBytes(size: Long) = apply {
        require(size > 0) { "size must be > 0." }
        this.maximumMaxSizeBytes = size
    }

    /**
     * Set the maximum size of the disk cache in bytes.
     */
    fun maxSizeBytes(size: Long) = apply {
        require(size > 0) { "size must be > 0." }
        this.maxSizePercent = 0.0
        this.maxSizeBytes = size
    }

    /**
     * Set the [CoroutineDispatcher] that cache size trim operations will be executed on.
     */
    fun cleanupDispatcher(dispatcher: CoroutineDispatcher) = apply {
        this.cleanupDispatcher = dispatcher
    }

    /**
     * Create a new [DiskCache] instance.
     */
    fun build(): DiskCache {
        val directory = checkNotNull(directory) { "directory == null" }
        val maxSize = if (maxSizePercent > 0) {
            try {
                val size = maxSizePercent * directorySize(directory)
                size.toLong().coerceIn(minimumMaxSizeBytes, maximumMaxSizeBytes)
            } catch (_: Exception) {
                minimumMaxSizeBytes
            }
        } else {
            maxSizeBytes
        }
        return RealDiskCache(
            maxSize = maxSize,
            directory = directory,
            fileSystem = fileSystem,
            cleanupDispatcher = cleanupDispatcher
        )
    }
}
