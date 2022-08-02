package com.seiko.imageloader.util

import okio.FileSystem

internal actual inline val systemFileSystem: FileSystem
    get() = FileSystem.SYSTEM
