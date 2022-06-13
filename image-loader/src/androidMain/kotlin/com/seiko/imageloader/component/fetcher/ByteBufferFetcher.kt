package com.seiko.imageloader.component.fetcher

import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.request.Options
import io.ktor.utils.io.ByteReadChannel
import java.nio.ByteBuffer

class ByteBufferFetcher(
    private val data: ByteBuffer,
) : Fetcher {
    override suspend fun fetch(): FetchResult {
        return SourceResult(
            source = ByteReadChannel(data),
            mimeType = null,
        )
    }

    class Factory : Fetcher.Factory {
        override fun create(data: Any, options: Options, imageLoader: ImageLoader): Fetcher? {
            if (data !is ByteBuffer) return null
            return ByteBufferFetcher(data)
        }
    }
}