package com.seiko.imageloader.util

import okio.Path

actual fun directorySize(directory: Path): Long {
    return 1000L * 1024 * 1024 // 1000MB
}
