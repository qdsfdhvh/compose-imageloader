package com.seiko.imageloader.util

import okio.Buffer
import okio.IOException
import okio.Sink

abstract class ForwardingSink(
    /** [Sink] to which this instance is delegating. */
    private val delegate: Sink,
) : Sink {
    // TODO 'Sink by delegate' once https://youtrack.jetbrains.com/issue/KT-23935 is fixed.

    @Throws(IOException::class)
    override fun write(source: Buffer, byteCount: Long) = delegate.write(source, byteCount)

    @Throws(IOException::class)
    override fun flush() = delegate.flush()

    override fun timeout() = delegate.timeout()

    @Throws(IOException::class)
    override fun close() = delegate.close()
}
