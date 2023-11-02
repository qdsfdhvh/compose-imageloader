package com.seiko.imageloader.component.fetcher

import androidx.compose.ui.graphics.painter.Painter
import com.seiko.imageloader.Bitmap
import com.seiko.imageloader.Image
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

fun Fetcher(block: suspend () -> FetchResult?) = object : Fetcher {
    override suspend fun fetch(): FetchResult? = block()
}

sealed interface FetchResult {
    @Poko class OfSource(
        val source: BufferedSource,
        val extra: ExtraData = EmptyExtraData,
    ) : FetchResult

    @Poko class OfBitmap(val bitmap: Bitmap) : FetchResult

    @Poko class OfImage(val image: Image) : FetchResult

    @Poko class OfPainter(val painter: Painter) : FetchResult
}
