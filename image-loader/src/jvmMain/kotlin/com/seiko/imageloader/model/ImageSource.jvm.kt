package com.seiko.imageloader.model

import okio.BufferedSource
import okio.buffer
import okio.source
import java.io.InputStream

interface InputStreamImageSource : ImageSource {
    val inputStream: InputStream
}

fun InputStreamImageSource(inputStream: InputStream): InputStreamImageSource {
    return object : InputStreamImageSource {
        override val inputStream: InputStream = inputStream
        override val bufferedSource: BufferedSource = inputStream.source().buffer()
        override fun close() {
            inputStream.close()
        }
    }
}

fun InputStream.toImageSource(): InputStreamImageSource {
    return InputStreamImageSource(this)
}
