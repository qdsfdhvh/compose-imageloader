package com.seiko.imageloader.component.fetcher

import com.seiko.imageloader.component.mapper.Base64Image
import com.seiko.imageloader.model.extraData
import com.seiko.imageloader.model.mimeType
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.util.bufferedSource

class Base64Fetcher private constructor(
    private val data: Base64Image,
) : Fetcher {
    override suspend fun fetch(): FetchResult {
        return FetchResult.Source(
            source = data.content.bufferedSource(),
            extra = extraData {
                mimeType(data.contentType)
            },
        )
    }

    class Factory : Fetcher.Factory {
        override fun create(data: Any, options: Options): Fetcher? {
            if (data !is Base64Image) return null
            return Base64Fetcher(data)
        }
    }
}
