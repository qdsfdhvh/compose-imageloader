package com.seiko.imageloader.component.mapper

import com.eygraber.uri.Uri
import com.seiko.imageloader.option.Options
import okio.Path
import okio.Path.Companion.toPath

class UriToPathMapper : Mapper<Path> {
    override fun map(data: Any, options: Options): Path? {
        if (data !is Uri) return null
        if (data.scheme != "file") return null
        return data.path?.toPath()
    }
}
