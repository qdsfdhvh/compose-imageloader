package com.seiko.imageloader.util

import android.os.StatFs
import okio.Path

actual fun directorySize(directory: Path): Long {
    val stats = StatFs(directory.toFile().absolutePath)
    return stats.blockCountLong * stats.blockSizeLong
}
