package com.seiko.imageloader.request

import com.seiko.imageloader.Image
import io.ktor.utils.io.ByteReadChannel

interface ImageResult {
    val request: ImageRequest
}

data class SourceResult(
    override val request: ImageRequest,
    val channel: ByteReadChannel,
    val mimeType: String? = null,
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
