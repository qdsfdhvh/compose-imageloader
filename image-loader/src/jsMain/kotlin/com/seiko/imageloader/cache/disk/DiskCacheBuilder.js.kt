package com.seiko.imageloader.cache.disk

import okio.FileSystem
import okio.NodeJsFileSystem
import okio.Path

internal actual inline val systemFileSystem: FileSystem
    get() = NodeJsFileSystem

internal actual fun directorySize(directory: Path): Long {
    return 128L * 1024 * 1024 // 128MB
}
