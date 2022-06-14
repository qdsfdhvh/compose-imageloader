package com.seiko.imageloader.component.decoder

import com.seiko.imageloader.request.Options
import com.seiko.imageloader.request.SourceResult

interface Decoder {
    suspend fun decode(): DecoderResult?
    fun interface Factory {
        fun create(source: SourceResult, options: Options): Decoder?
    }
}
