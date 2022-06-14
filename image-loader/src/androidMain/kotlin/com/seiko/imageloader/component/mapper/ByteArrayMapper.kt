package com.seiko.imageloader.component.mapper

import com.seiko.imageloader.request.Options
import java.nio.ByteBuffer

internal class ByteArrayMapper : Mapper<ByteBuffer> {
    override fun map(data: Any, options: Options): ByteBuffer? {
        if (data !is ByteArray) return null
        return ByteBuffer.wrap(data)
    }
}
