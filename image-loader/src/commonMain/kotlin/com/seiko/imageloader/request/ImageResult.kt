package com.seiko.imageloader.request

import com.seiko.imageloader.Image
import okio.BufferedSource

interface ImageResult {
    val request: ImageRequest
}

data class SourceResult(
    override val request: ImageRequest,
    val source: BufferedSource,
    val metadata: Any? = null,
) : ImageResult

data class SuccessResult(
    override val request: ImageRequest,
    val image: Image,
) : ImageResult

data class ErrorResult(
    override val request: ImageRequest,
    val error: Throwable,
) : ImageResult
