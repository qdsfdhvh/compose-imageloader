package com.seiko.imageloader.component.fetcher

import android.webkit.MimeTypeMap
import com.seiko.imageloader.request.Options
import okio.buffer
import okio.source
import java.io.File

internal class FileFetcher(private val data: File) : Fetcher {
    override suspend fun fetch(): FetchResult {
        return FetchSourceResult(
            source = data.source().buffer(),
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(data.extension),
        )
    }

    class Factory : Fetcher.Factory {
        override fun create(data: Any, options: Options): Fetcher? {
            if (data !is File) return null
            return FileFetcher(data)
        }
    }
}
