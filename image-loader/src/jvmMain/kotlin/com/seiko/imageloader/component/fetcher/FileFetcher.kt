package com.seiko.imageloader.component.fetcher

import com.seiko.imageloader.model.extraData
import com.seiko.imageloader.model.mimeType
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.util.getMimeTypeFromExtension
import okio.buffer
import okio.source
import java.io.File

class FileFetcher private constructor(
    private val data: File,
) : Fetcher {
    override suspend fun fetch(): FetchResult {
        return FetchResult.Source(
            source = data.source().buffer(),
            extra = extraData {
                mimeType(getMimeTypeFromExtension(data.extension))
            },
        )
    }

    class Factory : Fetcher.Factory {
        override fun create(data: Any, options: Options): Fetcher? {
            if (data !is File) return null
            return FileFetcher(data)
        }
    }
}
