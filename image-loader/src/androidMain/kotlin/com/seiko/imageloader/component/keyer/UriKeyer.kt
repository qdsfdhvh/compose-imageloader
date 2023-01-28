package com.seiko.imageloader.component.keyer

import android.content.ContentResolver.SCHEME_ANDROID_RESOURCE
import android.content.Context
import android.content.res.Configuration
import com.eygraber.uri.Uri
import com.seiko.imageloader.option.Options

class UriKeyer(private val context: Context) : Keyer {
    override fun key(data: Any, options: Options): String? {
        if (data !is Uri) return null

        if (data.scheme != SCHEME_ANDROID_RESOURCE) return data.toString()

        // android uri is mapper to android.resource://example.package.name/12345678,
        // but resId is changeable, here is convert resId to entryName.
        val resId = data.pathSegments.lastOrNull()?.toIntOrNull() ?: return data.toString()
        val entryName = context.resources.getResourceEntryName(resId)
        val newUri = data.buildUpon().path(entryName).build()

        // 'android.resource' uris can change if night mode is enabled/disabled.
        return "$newUri-${context.resources.configuration.nightMode}"
    }

    private val Configuration.nightMode: Int
        get() = uiMode and Configuration.UI_MODE_NIGHT_MASK
}
