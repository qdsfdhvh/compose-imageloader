package com.seiko.imageloader.component.fetcher

import com.seiko.imageloader.model.EmptyExtraData
import com.seiko.imageloader.model.ExtraData
import com.seiko.imageloader.option.Options
import okio.BufferedSource

interface Fetcher {
    suspend fun fetch(): FetchResult?
    fun interface Factory {
        fun create(data: Any, options: Options): Fetcher?
    }
}

sealed interface FetchResult {
    data class Source(
        val source: BufferedSource,
        val extra: ExtraData = EmptyExtraData,
    ) : FetchResult

    data class Bitmap(
        val bitmap: com.seiko.imageloader.Bitmap,
    ) : FetchResult

    data class Image(
        val image: com.seiko.imageloader.Image,
    ) : FetchResult

    data class Painter(
        val painter: androidx.compose.ui.graphics.painter.Painter,
    ) : FetchResult
}
