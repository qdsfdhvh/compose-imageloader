package com.seiko.imageloader.component.mapper

import android.content.ContentResolver
import android.net.Uri
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.util.firstPathSegment
import com.seiko.imageloader.util.isAssetUri
import java.io.File

class FileUriMapper : Mapper<File> {
    override fun map(data: Any, options: Options): File? {
        if (data !is Uri) return null
        if (!isApplicable(data)) return null
        return File(data.path!!)
    }

    private fun isApplicable(data: Uri): Boolean {
        return !isAssetUri(data) &&
            data.scheme.let { it == null || it == ContentResolver.SCHEME_FILE } &&
            data.path.orEmpty().startsWith('/') && data.firstPathSegment != null
    }
}
