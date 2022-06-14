package com.seiko.imageloader.request

import androidx.compose.ui.graphics.painter.Painter

interface ImageResult {
    val request: ImageRequest
}

// data class SourceResult(
//     override val result: ImageResult,
//
// ) : ImageResult

data class SuccessResult(
    override val request: ImageRequest,
    val painter: Painter,
) : ImageResult

data class ErrorResult(
    override val request: ImageRequest,
    val error: Throwable,
) : ImageResult
