package com.seiko.imageloader.cache.disk

import okio.FileSystem
import okio.Path

internal actual fun FileSystem.remainingFreeSpaceBytes(directory: Path): Long {
    return 512L * 1024 * 1024 // 512MB
}
