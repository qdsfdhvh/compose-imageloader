package com.seiko.imageloader.component.fetcher

import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.request.Options
import java.nio.ByteBuffer
import okio.Buffer

class ByteBufferFetcher(
    private val data: ByteBuffer,
) : Fetcher {
    override suspend fun fetch(): FetchResult {
        val source = try {
            Buffer().apply { write(data) }
        } finally {
            // Reset the position so we can read the byte buffer again.
            data.position(0)
        }
        return FetchSourceResult(
            source = source,
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