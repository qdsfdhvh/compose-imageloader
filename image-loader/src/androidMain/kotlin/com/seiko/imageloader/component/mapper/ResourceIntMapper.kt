package com.seiko.imageloader.component.mapper

import android.content.ContentResolver.SCHEME_ANDROID_RESOURCE
import android.content.Context
import android.content.res.Resources
import androidx.annotation.DrawableRes
import com.eygraber.uri.Uri
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.option.androidContext

class ResourceIntMapper(private val context: Context? = null) : Mapper<Uri> {

    override fun map(data: Any, options: Options): Uri? {
        if (data !is Int) return null
        val androidContext = context ?: options.androidContext
        if (!isApplicable(data, androidContext)) return null
        return Uri.parse("$SCHEME_ANDROID_RESOURCE://${androidContext.packageName}/$data")
    }

    private fun isApplicable(@DrawableRes data: Int, context: Context): Boolean {
        return try {
            context.resources.getResourceEntryName(data) != null
        } catch (_: Resources.NotFoundException) {
            false
        }
    }
}
