package com.seiko.imageloader.component.fetcher

import com.seiko.imageloader.option.Options
import okio.Buffer

class ByteArrayFetcher(
    private val data: ByteArray,
) : Fetcher {
    override suspend fun fetch(): FetchResult {
        return FetchResult.OfSource(
            source = Buffer().apply {
                write(data)
            },
        )
    }

    class Factory : Fetcher.Factory {
        override fun create(data: Any, options: Options): Fetcher? {
            if (data !is ByteArray) return null
            return ByteArrayFetcher(data)
        }
    }
}
