package com.seiko.imageloader.component.fetcher

import com.seiko.imageloader.component.mapper.Base64Image
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.util.bufferedSource

class Base64Fetcher(
    private val data: Base64Image,
) : Fetcher {
    override suspend fun fetch(): FetchResult {
        return FetchSourceResult(
            source = data.content.bufferedSource(),
            mimeType = data.contentType,
        )
    }

    class Factory : Fetcher.Factory {
        override fun create(data: Any, options: Options): Fetcher? {
            return if (data is Base64Image) {
                Base64Fetcher(data)
            } else null
        }
    }
}
