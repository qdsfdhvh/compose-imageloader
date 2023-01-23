package com.seiko.imageloader.component.keyer

import com.seiko.imageloader.option.Options
import java.io.File

class FileKeyer(
    private val addLastModifiedToFileCacheKey: Boolean = true,
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
