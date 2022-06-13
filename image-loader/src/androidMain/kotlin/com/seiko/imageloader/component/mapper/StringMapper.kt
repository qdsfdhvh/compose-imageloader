package com.seiko.imageloader.component.mapper

import android.net.Uri
import androidx.core.net.toUri
import com.seiko.imageloader.request.Options

class StringMapper : Mapper<String, Uri> {
    override fun map(data: String, options: Options): Uri {
        return data.toUri()
    }
}