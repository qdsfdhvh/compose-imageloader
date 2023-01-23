package com.seiko.imageloader.component.mapper

import android.content.ContentResolver.SCHEME_ANDROID_RESOURCE
import android.content.Context
import com.eygraber.uri.Uri
import com.seiko.imageloader.option.Options

/**
 * Maps android.resource uris with resource names to uris containing their resources ID. i.e.:
 *
 * android.resource://example.package.name/drawable/image -> android.resource://example.package.name/12345678
 */
class ResourceUriMapper(private val context: Context) : Mapper<Uri> {

    override fun map(data: Any, options: Options): Uri? {
        if (data !is Uri) return null
        if (!isApplicable(data)) return null

        val packageName = data.authority.orEmpty()
        val resources = context.packageManager.getResourcesForApplication(packageName)
        val (type, name) = data.pathSegments
        val id = resources.getIdentifier(name, type, packageName)
        check(id != 0) { "Invalid $SCHEME_ANDROID_RESOURCE URI: $data" }

        return Uri.parse("$SCHEME_ANDROID_RESOURCE://$packageName/$id")
    }

    private fun isApplicable(data: Uri): Boolean {
        return data.scheme == SCHEME_ANDROID_RESOURCE &&
            !data.authority.isNullOrBlank() &&
            data.pathSegments.count() == 2
    }
}
