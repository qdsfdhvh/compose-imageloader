package com.seiko.imageloader.component.mapper

import android.content.ContentResolver
import com.eygraber.uri.Uri
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.util.firstPathSegment
import com.seiko.imageloader.util.isAssetUri
import java.io.File

class FileUriMapper : Mapper<File> {
    override fun map(data: Any, options: Options): File? {
        if (data !is Uri) return null
        if (!isApplicable(data)) return null
        return data.path?.let { File(it) }
    }

    private fun isApplicable(data: Uri): Boolean {
        return !isAssetUri(data) &&
            data.scheme.let { it == null || it == ContentResolver.SCHEME_FILE } &&
            data.path.orEmpty().startsWith('/') && data.firstPathSegment != null
    }
}
