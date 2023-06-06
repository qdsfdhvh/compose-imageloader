package com.seiko.imageloader.cache.disk

import android.os.StatFs
import okio.Path

internal actual fun directorySize(directory: Path): Long {
    val stats = StatFs(directory.toFile().absolutePath)
    return stats.blockCountLong * stats.blockSizeLong
}
