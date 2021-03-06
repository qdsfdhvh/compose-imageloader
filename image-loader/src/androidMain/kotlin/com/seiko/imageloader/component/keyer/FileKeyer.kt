package com.seiko.imageloader.component.keyer

import com.seiko.imageloader.request.Options
import java.io.File

internal class FileKeyer(
    private val addLastModifiedToFileCacheKey: Boolean,
) : Keyer {
    override fun key(data: Any, options: Options): String? {
        if (data !is File) return null
        return if (addLastModifiedToFileCacheKey) {
            "${data.path}:${data.lastModified()}"
        } else {
            data.path
        }
    }
}
