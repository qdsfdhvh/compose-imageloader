package com.seiko.imageloader.component.mapper

import com.eygraber.uri.Uri
import com.seiko.imageloader.option.Options

class StringUriMapper : Mapper<Uri> {
    override fun map(data: Any, options: Options): Uri? {
        if (data !is String) return null
        // ignore data uri, see: https://en.wikipedia.org/wiki/Data_URI_scheme
        if (data.startsWith("data:")) return null
        // ignore http url
        if (data.startsWith("http:")) return null
        if (data.startsWith("https:")) return null
        return Uri.parseOrNull(data)
    }
}
