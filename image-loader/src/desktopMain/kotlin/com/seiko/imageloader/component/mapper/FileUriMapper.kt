package com.seiko.imageloader.component.mapper

import com.eygraber.uri.Uri
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.util.firstPathSegment
import java.io.File

class FileUriMapper : Mapper<File> {
    override fun map(data: Any, options: Options): File? {
        if (data !is Uri) return null
        if (!isApplicable(data)) return null
        return File(data.path)
    }

    private fun isApplicable(data: Uri): Boolean {
        return data.scheme.let { it == null || it == "file" } &&
            data.path.orEmpty().startsWith('/') && data.firstPathSegment != null
    }
}
