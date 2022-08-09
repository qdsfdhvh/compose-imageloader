package com.seiko.imageloader.cache.disk

import okio.FileSystem
import okio.Path
import platform.Foundation.NSFileManager
import platform.Foundation.NSFileSystemFreeSize
import platform.Foundation.NSNumber

internal actual inline val systemFileSystem: FileSystem
    get() = FileSystem.SYSTEM

internal actual fun directorySize(directory: Path): Long {
    val fileAttributes = NSFileManager.defaultManager.attributesOfFileSystemForPath(directory.toString(), null)
    val number = fileAttributes?.get(NSFileSystemFreeSize) as? NSNumber
    if (number != null) {
        return number.integerValue
    }
    return 512L * 1024 * 1024 // 512MB
}
