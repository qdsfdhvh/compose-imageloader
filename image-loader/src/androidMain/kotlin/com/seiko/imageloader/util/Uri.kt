package com.seiko.imageloader.util

import android.content.ContentResolver
import android.net.Uri

private const val ASSET_FILE_PATH_ROOT = "android_asset"

internal fun isAssetUri(uri: Uri): Boolean {
    return uri.scheme == ContentResolver.SCHEME_FILE && uri.firstPathSegment == ASSET_FILE_PATH_ROOT
}

internal val Uri.firstPathSegment: String?
    get() = pathSegments.firstOrNull()
