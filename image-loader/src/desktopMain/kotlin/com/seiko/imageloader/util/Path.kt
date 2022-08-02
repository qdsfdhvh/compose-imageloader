package com.seiko.imageloader.util

import okio.Path

actual fun directorySize(directory: Path): Long {
    return 10000L * 1024 * 1024 // 10000MB
}
