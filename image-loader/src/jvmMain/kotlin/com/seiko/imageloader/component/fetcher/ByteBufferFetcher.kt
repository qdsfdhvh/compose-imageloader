package com.seiko.imageloader.component.fetcher

import com.seiko.imageloader.model.ImageSourceFrom
import com.seiko.imageloader.model.extraData
import com.seiko.imageloader.model.metadata
import com.seiko.imageloader.model.toImageSource
import com.seiko.imageloader.option.Options
import okio.Buffer
import java.nio.ByteBuffer

class ByteBufferFetcher private constructor(
    private val data: ByteBuffer,
) : Fetcher {
    override suspend fun fetch(): FetchResult {
        val source = try {
            Buffer().apply { write(data) }
        } finally {
            // Reset the position so we can read the byte buffer again.
            data.position(0)
        }
        return FetchResult.OfSource(
            imageSource = source.buffer().toImageSource(),
            imageSourceFrom = ImageSourceFrom.Memory,
            extra = extraData {
                metadata(data)
            },
        )
    }

    class Metadata(
        val byteBuffer: ByteBuffer,
    )

    class Factory : Fetcher.Factory {
        override fun create(data: Any, options: Options): Fetcher? {
            if (data !is ByteBuffer) return null
            return ByteBufferFetcher(data)
        }
    }
}
