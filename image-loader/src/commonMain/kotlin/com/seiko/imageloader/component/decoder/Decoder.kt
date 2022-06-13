package com.seiko.imageloader.component.decoder

import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.component.fetcher.SourceResult
import com.seiko.imageloader.request.Options

interface Decoder {
    suspend fun decode(): DecoderResult?
    fun interface Factory {
        fun create(result: SourceResult, options: Options, imageLoader: ImageLoader): Decoder?
    }
}
