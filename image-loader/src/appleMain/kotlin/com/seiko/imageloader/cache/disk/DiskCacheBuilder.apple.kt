package com.seiko.imageloader.cache.disk

import kotlinx.cinterop.ExperimentalForeignApi
import okio.FileSystem
import okio.Path
import platform.Foundation.NSDictionary
import platform.Foundation.NSFileManager
import platform.Foundation.fileSize

@OptIn(ExperimentalForeignApi::class)
internal actual fun FileSystem.remainingFreeSpaceBytes(directory: Path): Long {
    val fileSize = NSFileManager.defaultManager.attributesOfFileSystemForPath(directory.toString(), null) as? NSDictionary
    if (fileSize != null) {
        return fileSize.fileSize().toLong()
    }
    return 512 * 1024 * 1024 // 512MB
}
