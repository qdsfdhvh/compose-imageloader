package com.seiko.imageloader.component.fetcher

import android.content.Context
import android.webkit.MimeTypeMap
import com.eygraber.uri.Uri
import com.seiko.imageloader.model.extraData
import com.seiko.imageloader.model.metadata
import com.seiko.imageloader.model.mimeType
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.util.getMimeTypeFromUrl
import com.seiko.imageloader.util.isAssetUri
import okio.buffer
import okio.source

class AssetUriFetcher private constructor(
    private val context: Context,
    private val data: Uri,
) : Fetcher {

    override suspend fun fetch(): FetchResult {
        val path = data.pathSegments.drop(1).joinToString("/")
        return FetchResult.Source(
            source = context.assets.open(path).source().buffer(),
            extra = extraData {
                mimeType(MimeTypeMap.getSingleton().getMimeTypeFromUrl(path))
                metadata(data.lastPathSegment?.let { MetaData(it) })
            },
        )
    }

    class Factory(private val context: Context) : Fetcher.Factory {
        override fun create(data: Any, options: Options): Fetcher? {
            if (data !is Uri) return null
            if (!isAssetUri(data)) return null
            return AssetUriFetcher(context, data)
        }
    }

    data class MetaData(
        val fileName: String,
    )
}
