package com.seiko.imageloader.component.fetcher

import android.content.Context
import android.webkit.MimeTypeMap
import com.eygraber.uri.Uri
import com.seiko.imageloader.model.ImageSourceFrom
import com.seiko.imageloader.model.extraData
import com.seiko.imageloader.model.metadata
import com.seiko.imageloader.model.mimeType
import com.seiko.imageloader.model.toImageSource
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.option.androidContext
import com.seiko.imageloader.util.getMimeTypeFromUrl
import com.seiko.imageloader.util.isAssetUri
import dev.drewhamilton.poko.Poko

class AssetUriFetcher private constructor(
    private val context: Context,
    private val data: Uri,
) : Fetcher {

    override suspend fun fetch(): FetchResult {
        val path = data.pathSegments.drop(1).joinToString("/")
        return FetchResult.OfSource(
            imageSource = context.assets.open(path).toImageSource(),
            imageSourceFrom = ImageSourceFrom.Disk,
            extra = extraData {
                mimeType(MimeTypeMap.getSingleton().getMimeTypeFromUrl(path))
                metadata(data.lastPathSegment?.let { MetaData(it) })
            },
        )
    }

    class Factory(
        private val context: Context? = null,
    ) : Fetcher.Factory {
        override fun create(data: Any, options: Options): Fetcher? {
            if (data !is Uri) return null
            if (!isAssetUri(data)) return null
            return AssetUriFetcher(
                context = context ?: options.androidContext,
                data = data,
            )
        }
    }

    @Poko
    class MetaData(
        val fileName: String,
    )
}
