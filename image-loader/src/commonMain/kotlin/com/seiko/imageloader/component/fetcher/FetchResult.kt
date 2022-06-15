package com.seiko.imageloader.component.fetcher

import com.seiko.imageloader.Image
import io.ktor.utils.io.ByteReadChannel

sealed interface FetchResult

class FetchSourceResult(
    val source: ByteReadChannel,
    val mimeType: String?,
    val metadata: Any? = null,
) : FetchResult

class FetchImageResult(
    val image: Image,
) : FetchResult
