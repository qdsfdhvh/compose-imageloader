package com.seiko.imageloader.component.decoder

import androidx.compose.ui.geometry.isSpecified
import com.seiko.imageloader.model.ImageSource
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.util.calculateDstSize
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import okio.use
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Canvas
import org.jetbrains.skia.Image
import org.jetbrains.skia.Rect
import org.jetbrains.skia.impl.use

class SkiaImageDecoder private constructor(
    private val source: ImageSource,
    private val options: Options,
    private val parallelismLock: Semaphore,
) : Decoder {

    override suspend fun decode() = parallelismLock.withPermit {
        val image = source.bufferedSource.use {
            Image.makeFromEncoded(it.readByteArray())
        }
        DecodeResult.OfBitmap(
            bitmap = image.toBitmap(),
        )
    }

    // TODO wait to fix high probability crash on ios
    private fun Image.toBitmap(): Bitmap {
        val bitmap = Bitmap()

        val srcWidth = width
        val srcHeight = height

        val maxImageSize = if (options.size.isSpecified && !options.size.isEmpty()) {
            minOf(options.size.width, options.size.height).toInt()
                .coerceAtMost(options.maxImageSize)
        } else {
            options.maxImageSize
        }
        val (dstWidth, dstHeight) = calculateDstSize(srcWidth, srcHeight, maxImageSize)

        bitmap.allocN32Pixels(dstWidth, dstHeight)
        Canvas(bitmap).use { canvas ->
            canvas.drawImageRect(
                this,
                Rect.makeWH(srcWidth.toFloat(), srcHeight.toFloat()),
                Rect.makeWH(dstWidth.toFloat(), dstHeight.toFloat()),
            )
        }
        bitmap.setImmutable()
        return bitmap
    }

    class Factory(
        maxParallelism: Int = Options.DEFAULT_MAX_PARALLELISM,
    ) : Decoder.Factory {

        private val parallelismLock = Semaphore(maxParallelism)

        override fun create(source: DecodeSource, options: Options): Decoder {
            return SkiaImageDecoder(
                source = source.imageSource,
                options = options,
                parallelismLock = parallelismLock,
            )
        }
    }
}
