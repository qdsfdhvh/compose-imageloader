package com.seiko.imageloader.component.fetcher

import android.content.ContentResolver.SCHEME_ANDROID_RESOURCE
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.os.Build.VERSION.SDK_INT
import android.util.TypedValue
import android.util.Xml
import android.webkit.MimeTypeMap
import androidx.annotation.DrawableRes
import androidx.annotation.XmlRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.eygraber.uri.Uri
import com.seiko.imageloader.model.extraData
import com.seiko.imageloader.model.metadata
import com.seiko.imageloader.model.mimeType
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.toImage
import com.seiko.imageloader.util.DrawableUtils
import com.seiko.imageloader.util.getMimeTypeFromUrl
import com.seiko.imageloader.util.toBitmapConfig
import okio.buffer
import okio.source
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException

class ResourceUriFetcher private constructor(
    private val context: Context,
    private val data: Uri,
    private val options: Options,
) : Fetcher {

    override suspend fun fetch(): FetchResult {
        // Expected format: android.resource://example.package.name/12345678
        val packageName =
            data.authority?.takeIf { it.isNotBlank() } ?: throwInvalidUriException(data)
        val resId = data.pathSegments.lastOrNull()?.toIntOrNull() ?: throwInvalidUriException(data)

        val resources = if (packageName == context.packageName) {
            context.resources
        } else {
            context.packageManager.getResourcesForApplication(packageName)
        }
        val path = TypedValue().apply { resources.getValue(resId, this, true) }.string
        val entryName = path.substring(path.lastIndexOf('/'))
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromUrl(entryName)

        return if (mimeType == MIME_TYPE_XML) {
            // getDrawableCompat can only load resources that are in the current package.
            val drawable = if (packageName == context.packageName) {
                context.getDrawableCompat(resId)
            } else {
                context.getXmlDrawableCompat(resources, resId)
            }

            val isVector = drawable.isVector
            if (isVector) {
                FetchResult.Bitmap(
                    bitmap = DrawableUtils.convertToBitmap(
                        drawable = drawable,
                        config = options.config.toBitmapConfig(),
                        scale = options.scale,
                        allowInexactSize = options.allowInexactSize,
                    ),
                )
            } else if (drawable is BitmapDrawable) {
                FetchResult.Bitmap(
                    bitmap = drawable.bitmap,
                )
            } else {
                FetchResult.Image(
                    image = drawable.toImage(),
                )
            }
        } else {
            val typedValue = TypedValue()
            val inputStream = resources.openRawResource(resId, typedValue)
            FetchResult.Source(
                source = inputStream.source().buffer(),
                extra = extraData {
                    mimeType(mimeType)
                    metadata(Metadata(packageName, resId, typedValue.density))
                },
            )
        }
    }

    private val Drawable.isVector: Boolean
        get() = this is VectorDrawable || this is VectorDrawableCompat

    private fun throwInvalidUriException(data: Uri): Nothing {
        throw IllegalStateException("Invalid $SCHEME_ANDROID_RESOURCE URI: $data")
    }

    class Factory(private val context: Context) : Fetcher.Factory {
        override fun create(data: Any, options: Options): Fetcher? {
            if (data !is Uri) return null
            if (!isApplicable(data)) return null
            return ResourceUriFetcher(context, data, options)
        }

        private fun isApplicable(data: Uri): Boolean {
            return data.scheme == SCHEME_ANDROID_RESOURCE
        }
    }

    data class Metadata(
        val packageName: String,
        @DrawableRes val resId: Int,
        val density: Int,
    )

    companion object {
        private const val MIME_TYPE_XML = "text/xml"
    }
}

private fun Context.getDrawableCompat(@DrawableRes resId: Int): Drawable {
    return checkNotNull(
        AppCompatResources.getDrawable(
            this,
            resId,
        ),
    ) { "Invalid resource ID: $resId" }
}

private fun Resources.getDrawableCompat(resId: Int, theme: Resources.Theme?): Drawable {
    return checkNotNull(
        ResourcesCompat.getDrawable(
            this,
            resId,
            theme,
        ),
    ) { "Invalid resource ID: $resId" }
}

private fun Context.getXmlDrawableCompat(resources: Resources, @XmlRes resId: Int): Drawable {
    // Find the XML's start tag.
    val parser = resources.getXml(resId)
    var type = parser.next()
    while (type != XmlPullParser.START_TAG && type != XmlPullParser.END_DOCUMENT) {
        type = parser.next()
    }
    if (type != XmlPullParser.START_TAG) {
        throw XmlPullParserException("No start tag found.")
    }

    // Modified from androidx.appcompat.widget.ResourceManagerInternal.
    if (SDK_INT < 24) {
        when (parser.name) {
            "vector" -> {
                return VectorDrawableCompat.createFromXmlInner(
                    resources,
                    parser,
                    Xml.asAttributeSet(parser),
                    theme,
                )
            }
            "animated-vector" -> {
                return AnimatedVectorDrawableCompat.createFromXmlInner(
                    this,
                    resources,
                    parser,
                    Xml.asAttributeSet(parser),
                    theme,
                )
            }
        }
    }

    // Fall back to the platform APIs.
    return resources.getDrawableCompat(resId, theme)
}
