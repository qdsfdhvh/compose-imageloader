package com.seiko.imageloader.component.fetcher

import com.seiko.imageloader.model.extraData
import com.seiko.imageloader.model.mimeType
import com.seiko.imageloader.option.Options
import io.ktor.util.decodeBase64Bytes
import okio.Buffer

class Base64Fetcher private constructor(
    private val data: String,
) : Fetcher {
    override suspend fun fetch(): FetchResult {
        return data.split(',').let {
            val contentType = it.firstOrNull()?.removePrefix("data:")?.removeSuffix(";base64")
            val content = it.last()
            FetchResult.Source(
                source = Buffer().apply {
                    write(content.decodeBase64Bytes())
                },
                extra = extraData {
                    mimeType(contentType)
                },
            )
        }
    }

    class Factory : Fetcher.Factory {
        override fun create(data: Any, options: Options): Fetcher? {
            if (data !is String) return null
            if (!isApplicable(data)) return null
            return Base64Fetcher(data)
        }

        private fun isApplicable(data: String): Boolean {
            return data.startsWith("data:")
        }
    }
}
