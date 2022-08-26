package com.seiko.imageloader.component.mapper

import com.eygraber.uri.Uri
import com.seiko.imageloader.request.Options

class StringUriMapper : Mapper<Uri> {
    override fun map(data: Any, options: Options): Uri? {
        if (data !is String) return null
        return Uri.parse(data)
    }
}
