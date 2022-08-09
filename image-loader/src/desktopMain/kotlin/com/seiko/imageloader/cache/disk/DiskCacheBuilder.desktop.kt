package com.seiko.imageloader.cache.disk

import okio.FileSystem
import okio.Path

internal actual inline val systemFileSystem: FileSystem
    get() = FileSystem.SYSTEM

internal actual fun directorySize(directory: Path): Long {
    return 512L * 1024 * 1024 // 512MB
}
