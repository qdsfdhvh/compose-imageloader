package com.seiko.imageloader.component.mapper

import android.content.ContentResolver.SCHEME_ANDROID_RESOURCE
import android.content.Context
import android.content.res.Resources
import androidx.annotation.DrawableRes
import com.eygraber.uri.Uri
import com.seiko.imageloader.option.Options

class ResourceIntMapper(private val context: Context) : Mapper<Uri> {

    override fun map(data: Any, options: Options): Uri? {
        if (data !is Int) return null
        if (!isApplicable(data, context)) return null
        return Uri.parse("$SCHEME_ANDROID_RESOURCE://${context.packageName}/$data")
    }

    private fun isApplicable(@DrawableRes data: Int, context: Context): Boolean {
        return try {
            context.resources.getResourceEntryName(data) != null
        } catch (_: Resources.NotFoundException) {
            false
        }
    }
}
