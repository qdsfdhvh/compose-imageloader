package com.seiko.imageloader.component.fetcher

import android.content.Context
import android.webkit.MimeTypeMap
import com.eygraber.uri.Uri
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.util.getMimeTypeFromUrl
import com.seiko.imageloader.util.isAssetUri
import okio.buffer
import okio.source

internal class AssetUriFetcher(
    private val context: Context,
    private val data: Uri,
) : Fetcher {

    override suspend fun fetch(): FetchResult {
        val path = data.pathSegments.drop(1).joinToString("/")
        return FetchSourceResult(
            source = context.assets.open(path).source().buffer(),
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromUrl(path),
            metadata = AssetMetadata(data.lastPathSegment!!),
        )
    }

    class Factory(private val context: Context) : Fetcher.Factory {
        override fun create(data: Any, options: Options): Fetcher? {
            if (data !is Uri) return null
            if (!isAssetUri(data)) return null
            return AssetUriFetcher(context, data)
        }
    }
}

data class AssetMetadata(val fileName: String)
