package com.seiko.imageloader.model

import okio.Buffer
import okio.BufferedSource

interface ImageSource : AutoCloseable {
    val bufferedSource: BufferedSource
}

fun ImageSource(source: BufferedSource): ImageSource = object : ImageSource {
    override val bufferedSource: BufferedSource = source

    override fun close() {
        source.close()
    }
}

fun ImageSource(byteArray: ByteArray): ImageSource = object : ImageSource {
    override val bufferedSource: BufferedSource = Buffer().apply {
        write(byteArray)
    }

    override fun close() {
        // no-op
    }
}

fun BufferedSource.toImageSource(): ImageSource = ImageSource(this)

fun ByteArray.toImageSource(): ImageSource = ImageSource(this)
