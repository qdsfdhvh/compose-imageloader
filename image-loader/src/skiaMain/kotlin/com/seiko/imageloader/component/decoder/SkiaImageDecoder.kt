package com.seiko.imageloader.component.decoder

import com.seiko.imageloader.option.Options
import com.seiko.imageloader.util.DEFAULT_MAX_PARALLELISM
import com.seiko.imageloader.util.calculateDstSize
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import okio.BufferedSource
import okio.use
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Canvas
import org.jetbrains.skia.Image
import org.jetbrains.skia.Rect
import org.jetbrains.skia.SamplingMode
import org.jetbrains.skia.impl.use

class SkiaImageDecoder private constructor(
    private val source: BufferedSource,
    private val options: Options,
    private val parallelismLock: Semaphore,
) : Decoder {

    override suspend fun decode() = parallelismLock.withPermit {
        val image = source.use {
            Image.makeFromEncoded(it.readByteArray())
        }
        DecodeResult.Bitmap(
            bitmap = image.toBitmap(),
        )
    }

    // TODO wait to fix high probability crash on ios
    private fun Image.toBitmap(): Bitmap {
        val bitmap = Bitmap()
        val (dstWidth, dstHeight) = calculateDstSize(width, height, options.maxImageSize)
        bitmap.allocN32Pixels(dstWidth, dstHeight)
        Canvas(bitmap).use { canvas ->
            canvas.drawImageRect(
                this,
                Rect.makeWH(width.toFloat(), height.toFloat()),
                Rect.makeWH(dstWidth.toFloat(), dstHeight.toFloat()),
                SamplingMode.DEFAULT,
                null,
                true,
            )
        }
        bitmap.setImmutable()
        return bitmap
    }

    class Factory(
        maxParallelism: Int = DEFAULT_MAX_PARALLELISM,
    ) : Decoder.Factory {

        private val parallelismLock = Semaphore(maxParallelism)

        override suspend fun create(source: DecodeSource, options: Options): Decoder {
            return SkiaImageDecoder(
                source = source.source,
                options = options,
                parallelismLock = parallelismLock,
            )
        }
    }
}
