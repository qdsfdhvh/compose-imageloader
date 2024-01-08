package com.seiko.imageloader.component.mapper

import com.seiko.imageloader.option.Options
import okio.Path
import okio.Path.Companion.toPath
import platform.Foundation.NSURL

class NSURLToPathMapper : Mapper<Path> {
    override fun map(data: Any, options: Options): Path? {
        if (data !is NSURL) return null
        return data.path?.toPath()
    }
}
