package com.seiko.imageloader.util

import android.webkit.MimeTypeMap

internal actual fun getMimeTypeFromExtension(extension: String): String? {
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
}
