package com.seiko.imageloader.component.mapper

import com.eygraber.uri.Uri
import com.eygraber.uri.toUri
import com.seiko.imageloader.option.Options
import platform.Foundation.NSURL

class NSURLToUriMapper : Mapper<Uri> {
    override fun map(data: Any, options: Options): Uri? {
        if (data !is NSURL) return null
        return data.toUri()
    }
}
