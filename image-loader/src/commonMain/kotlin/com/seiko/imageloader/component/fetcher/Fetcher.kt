package com.seiko.imageloader.component.fetcher

import com.seiko.imageloader.Poko
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
    @Poko class Source(
        val source: BufferedSource,
        val extra: ExtraData = EmptyExtraData,
    ) : FetchResult

    @Poko class Bitmap(
        val bitmap: com.seiko.imageloader.Bitmap,
    ) : FetchResult

    @Poko class Image(
        val image: com.seiko.imageloader.Image,
    ) : FetchResult

    @Poko class Painter(
        val painter: androidx.compose.ui.graphics.painter.Painter,
    ) : FetchResult
}
