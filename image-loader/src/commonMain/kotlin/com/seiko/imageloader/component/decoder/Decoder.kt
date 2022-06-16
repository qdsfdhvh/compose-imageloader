package com.seiko.imageloader.component.decoder

import com.seiko.imageloader.request.Options
import com.seiko.imageloader.request.SourceResult

interface Decoder {
    suspend fun decode(): DecoderResult?
    interface Factory {
        suspend fun create(source: SourceResult, options: Options): Decoder?
    }
}
