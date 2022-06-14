package com.seiko.imageloader.component.fetcher

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.util.getMimeTypeFromUrl
import com.seiko.imageloader.util.isAssetUri
import io.ktor.utils.io.jvm.javaio.toByteReadChannel

internal class AssetUriFetcher(
    private val context: Context,
    private val data: Uri,
) : Fetcher {

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun fetch(): FetchResult {
        val path = data.pathSegments.drop(1).joinToString("/")
        return FetchSourceResult(
            source = context.assets.open(path).toByteReadChannel(),
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromUrl(path),
        )
    }

    class Factory(private val context: Context) : Fetcher.Factory {
        override fun create(data: Any, options: Options, imageLoader: ImageLoader): Fetcher? {
            if (data !is Uri) return null
            if (!isAssetUri(data)) return null
            return AssetUriFetcher(context, data)
        }
    }
}
