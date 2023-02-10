package com.seiko.imageloader.cache.disk

import okio.FileSystem
import okio.Path
import okio.fakefilesystem.FakeFileSystem

private val fakeFileSystem by lazy { FakeFileSystem() }

internal actual inline val systemFileSystem: FileSystem
    get() = fakeFileSystem

internal actual fun directorySize(directory: Path): Long {
    return 128L * 1024 * 1024 // 128MB
}
