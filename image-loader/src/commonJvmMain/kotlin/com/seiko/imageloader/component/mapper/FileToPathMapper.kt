package com.seiko.imageloader.component.mapper

import com.seiko.imageloader.option.Options
import okio.Path
import okio.Path.Companion.toOkioPath
import java.io.File

class FileToPathMapper : Mapper<Path> {
    override fun map(data: Any, options: Options): Path? {
        if (data !is File) return null
        return data.toOkioPath()
    }
}
