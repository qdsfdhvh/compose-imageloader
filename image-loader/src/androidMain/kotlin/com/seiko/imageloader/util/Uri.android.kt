package com.seiko.imageloader.util

import android.content.ContentResolver
import com.eygraber.uri.Uri

private const val ASSET_FILE_PATH_ROOT = "android_asset"

internal fun isAssetUri(uri: Uri): Boolean {
    return uri.scheme == ContentResolver.SCHEME_FILE && uri.firstPathSegment == ASSET_FILE_PATH_ROOT
}
