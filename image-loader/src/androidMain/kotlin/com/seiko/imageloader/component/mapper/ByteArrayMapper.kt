package com.seiko.imageloader.component.mapper

import com.seiko.imageloader.request.Options
import java.nio.ByteBuffer

internal class ByteArrayMapper : Mapper<ByteArray, ByteBuffer> {
    override fun map(data: ByteArray, options: Options): ByteBuffer = ByteBuffer.wrap(data)
}
