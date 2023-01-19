package com.seiko.imageloader.component.decoder

import com.seiko.imageloader.option.Options
import com.seiko.imageloader.model.SourceResult

interface Decoder {
    suspend fun decode(): DecodeResult?
    interface Factory {
        suspend fun create(source: SourceResult, options: Options): Decoder?
    }
}
