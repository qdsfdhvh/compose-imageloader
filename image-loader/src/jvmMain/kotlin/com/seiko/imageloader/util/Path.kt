package com.seiko.imageloader.util

import okio.Path

actual fun directorySize(directory: Path): Long {
    return Long.MAX_VALUE // don't care for desktop
}
