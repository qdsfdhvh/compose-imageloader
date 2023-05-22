package com.seiko.imageloader.cache.disk

import okio.Path

internal actual fun directorySize(directory: Path): Long {
    return 512L * 1024 * 1024 // 512MB
}
