package com.seiko.imageloader.util

import okio.FileSystem

actual val systemFileSystem: FileSystem
    get() = FileSystem.SYSTEM
