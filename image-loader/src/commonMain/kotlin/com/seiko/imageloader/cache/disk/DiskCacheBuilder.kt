package com.seiko.imageloader.cache.disk

import com.seiko.imageloader.util.ioDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import okio.FileSystem
import okio.Path

class DiskCacheBuilder internal constructor() {

    private var directory: Path? = null
    private var fileSystem = systemFileSystem
    private var maxSizePercent = 0.02 // 2%
    private var minimumMaxSizeBytes = 10L * 1024 * 1024 // 10MB
    private var maximumMaxSizeBytes = 250L * 1024 * 1024 // 250MB
    private var maxSizeBytes = 0L
    private var cleanupDispatcher = ioDispatcher

    /**
     * Set the [directory] where the cache stores its data.
     *
     * IMPORTANT: It is an error to have two [DiskCache] instances active in the same
     * directory at the same time as this can corrupt the disk cache.
     */
    fun directory(directory: Path) {
        this.directory = directory
    }

    fun fileSystem(fileSystem: FileSystem) {
        this.fileSystem = fileSystem
    }

    fun maxSizePercent(percent: Double) {
        require(percent in 0.0..1.0) { "size must be in the range [0.0, 1.0]." }
        this.maxSizeBytes = 0
        this.maxSizePercent = percent
    }

    fun minimumMaxSizeBytes(size: Long) {
        require(size > 0) { "size must be > 0." }
        this.minimumMaxSizeBytes = size
    }

    fun maximumMaxSizeBytes(size: Long) {
        require(size > 0) { "size must be > 0." }
        this.maximumMaxSizeBytes = size
    }

    fun maxSizeBytes(size: Long) {
        require(size > 0) { "size must be > 0." }
        this.maxSizePercent = 0.0
        this.maxSizeBytes = size
    }

    fun cleanupDispatcher(dispatcher: CoroutineDispatcher) {
        this.cleanupDispatcher = dispatcher
    }

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
            cleanupDispatcher = cleanupDispatcher,
        )
    }
}

fun DiskCache(block: DiskCacheBuilder.() -> Unit) =
    DiskCacheBuilder().apply(block).build()

internal expect val systemFileSystem: FileSystem

internal expect fun directorySize(directory: Path): Long
