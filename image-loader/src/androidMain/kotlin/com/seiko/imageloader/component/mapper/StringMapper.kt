package com.seiko.imageloader.component.mapper

import android.net.Uri
import androidx.core.net.toUri
import com.seiko.imageloader.request.Options

class StringMapper : Mapper<Uri> {
    override fun map(data: Any, options: Options): Uri? {
        if (data !is String) return null
        return data.toUri()
    }
}
