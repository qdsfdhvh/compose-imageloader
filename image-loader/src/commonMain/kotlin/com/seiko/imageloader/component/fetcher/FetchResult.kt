package com.seiko.imageloader.component.fetcher

import com.seiko.imageloader.Image
import okio.BufferedSource

sealed interface FetchResult

class FetchSourceResult(
    val source: BufferedSource,
    val mimeType: String?,
    val metadata: Any? = null,
) : FetchResult

class FetchPainterResult(
    val image: Image,
) : FetchResult
