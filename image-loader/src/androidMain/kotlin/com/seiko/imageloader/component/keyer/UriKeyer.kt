package com.seiko.imageloader.component.keyer

import android.content.ContentResolver.SCHEME_ANDROID_RESOURCE
import android.content.Context
import android.content.res.Configuration
import com.eygraber.uri.Uri
import com.seiko.imageloader.option.Options

internal class UriKeyer(private val context: Context) : Keyer {
    override fun key(data: Any, options: Options): String? {
        if (data !is Uri) return null
        // 'android.resource' uris can change if night mode is enabled/disabled.
        return if (data.scheme == SCHEME_ANDROID_RESOURCE) {
            "$data-${context.resources.configuration.nightMode}"
        } else {
            data.toString()
        }
    }

    private val Configuration.nightMode: Int
        get() = uiMode and Configuration.UI_MODE_NIGHT_MASK
}
