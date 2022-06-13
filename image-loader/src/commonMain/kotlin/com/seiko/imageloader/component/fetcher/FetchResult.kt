package com.seiko.imageloader.component.fetcher

import androidx.compose.ui.graphics.painter.Painter
import io.ktor.utils.io.ByteReadChannel

sealed interface FetchResult

class SourceResult(
    val source: ByteReadChannel,
    val mimeType: String?,
    val metadata: Any? = null,
) : FetchResult

class PainterResult(
    val painter: Painter,
) : FetchResult
