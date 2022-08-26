package com.seiko.imageloader.util

import com.eygraber.uri.Uri

internal val Uri.firstPathSegment: String?
    get() = pathSegments.firstOrNull()
