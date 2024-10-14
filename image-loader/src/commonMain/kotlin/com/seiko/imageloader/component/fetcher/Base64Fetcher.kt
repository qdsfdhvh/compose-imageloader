package com.seiko.imageloader.component.fetcher

import com.seiko.imageloader.model.ImageSourceFrom
import com.seiko.imageloader.model.extraData
import com.seiko.imageloader.model.mimeType
import com.seiko.imageloader.model.toImageSource
import com.seiko.imageloader.option.Options
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class Base64Fetcher private constructor(
    private val data: String,
) : Fetcher {
    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun fetch(): FetchResult {
        return data.split(',').let {
            val contentType = it.firstOrNull()?.removePrefix("data:")?.removeSuffix(";base64")
            val content = it.last()
            FetchResult.OfSource(
                imageSource = Base64.decode(content).toImageSource(),
                imageSourceFrom = ImageSourceFrom.Memory,
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
