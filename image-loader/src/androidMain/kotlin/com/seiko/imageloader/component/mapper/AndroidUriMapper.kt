package com.seiko.imageloader.component.mapper

import com.eygraber.uri.Uri
import com.eygraber.uri.toUriOrNull
import com.seiko.imageloader.option.Options
import android.net.Uri as AndroidUri

class AndroidUriMapper : Mapper<Uri> {
    override fun map(data: Any, options: Options): Uri? {
        if (data !is AndroidUri) return null
        return data.toUriOrNull()
    }
}
