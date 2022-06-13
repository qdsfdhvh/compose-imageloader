package com.seiko.imageloader.component.mapper

import android.content.ContentResolver.SCHEME_ANDROID_RESOURCE
import android.content.Context
import android.content.res.Resources
import android.net.Uri
import androidx.annotation.DrawableRes
import com.seiko.imageloader.request.Options

internal class ResourceIntMapper(private val context: Context): Mapper<Int, Uri> {

    override fun map(@DrawableRes data: Int, options: Options): Uri? {
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
