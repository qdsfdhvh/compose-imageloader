package com.seiko.imageloader.component.decoder

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build.VERSION.SDK_INT
import androidx.compose.ui.geometry.isSpecified
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.option.androidContext
import com.seiko.imageloader.util.ExifData
import com.seiko.imageloader.util.ExifUtils
import com.seiko.imageloader.util.calculateDstSize
import com.seiko.imageloader.util.calculateInSampleSize
import com.seiko.imageloader.util.computeSizeMultiplier
import com.seiko.imageloader.util.isRotated
import com.seiko.imageloader.util.isSwapped
import com.seiko.imageloader.util.toAndroidConfig
import com.seiko.imageloader.util.toSoftware
import kotlinx.coroutines.runInterruptible
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import okio.Buffer
import okio.ForwardingSource
import okio.Source
import okio.buffer
import kotlin.math.roundToInt

/** The base [Decoder] that uses [BitmapFactory] to decode a given [ImageSource]. */
class BitmapFactoryDecoder private constructor(
    private val context: Context,
    private val source: DecodeSource,
    private val options: Options,
    private val parallelismLock: Semaphore,
) : Decoder {

    override suspend fun decode() = parallelismLock.withPermit {
        runInterruptible { BitmapFactory.Options().decode() }
    }

    private fun BitmapFactory.Options.decode(): DecodeResult {
        val safeSource = ExceptionCatchingSource(source.source)
        val safeBufferedSource = safeSource.buffer()

        // Read the image's dimensions.
        inJustDecodeBounds = true
        BitmapFactory.decodeStream(safeBufferedSource.peek().inputStream(), null, this)
        safeSource.exception?.let { throw it }
        inJustDecodeBounds = false

        // Read the image's EXIF data.
        val exifData = ExifUtils.readData(outMimeType, safeBufferedSource)
        safeSource.exception?.let { throw it }

        // Always create immutable bitmaps as they have better performance.
        inMutable = false

        // if (SDK_INT >= 26 && options.colorSpace != null) {
        //     inPreferredColorSpace = options.colorSpace
        // }
        inPremultiplied = options.premultipliedAlpha

        configureConfig(exifData)
        configureScale(exifData)

        // Decode the bitmap.
        val outBitmap: Bitmap? = safeBufferedSource.use {
            BitmapFactory.decodeStream(it.inputStream(), null, this)
        }
        safeSource.exception?.let { throw it }
        checkNotNull(outBitmap) {
            "BitmapFactory returned a null bitmap. Often this means BitmapFactory could not " +
                "decode the image data read from the input source (e.g. network, disk, or " +
                "memory) as it's not encoded as a valid image format."
        }

        // Fix the incorrect density created by overloading inDensity/inTargetDensity.
        outBitmap.density = context.resources.displayMetrics.densityDpi

        // Reverse the EXIF transformations to get the original image.
        val bitmap = ExifUtils.reverseTransformations(outBitmap, exifData)
        return DecodeResult.OfBitmap(
            bitmap = bitmap,
        )
    }

    /** Compute and set [BitmapFactory.Options.inPreferredConfig]. */
    private fun BitmapFactory.Options.configureConfig(exifData: ExifData) {
        var config = options.bitmapConfig.toAndroidConfig()

        // Disable hardware bitmaps if we need to perform EXIF transformations.
        if (exifData.isFlipped || exifData.isRotated) {
            config = config.toSoftware()
        }

        // Decode the image as RGB_565 as an optimization if allowed.
        // if (options.allowRgb565 && config == Bitmap.Config.ARGB_8888 && outMimeType == MIME_TYPE_JPEG) {
        //     config = Bitmap.Config.RGB_565
        // }

        // High color depth images must be decoded as either RGBA_F16 or HARDWARE.
        if (SDK_INT >= 26 && outConfig == Bitmap.Config.RGBA_F16 && config != Bitmap.Config.HARDWARE) {
            config = Bitmap.Config.RGBA_F16
        }

        inPreferredConfig = config
    }

    /** Compute and set the scaling properties for [BitmapFactory.Options]. */
    private fun BitmapFactory.Options.configureScale(exifData: ExifData) {
        // This occurs if there was an error decoding the image's size.
        if (outWidth <= 0 || outHeight <= 0) {
            inSampleSize = 1
            inScaled = false
            return
        }

        // srcWidth and srcHeight are the original dimensions of the image after
        // EXIF transformations (but before sampling).
        val srcWidth = if (exifData.isSwapped) outHeight else outWidth
        val srcHeight = if (exifData.isSwapped) outWidth else outHeight

        val maxImageSize = if (options.size.isSpecified && !options.size.isEmpty()) {
            minOf(options.size.width, options.size.height).toInt()
                .coerceAtMost(options.maxImageSize)
        } else {
            options.maxImageSize
        }
        val (dstWidth, dstHeight) = calculateDstSize(srcWidth, srcHeight, maxImageSize)

        // Calculate the image's sample size.
        inSampleSize = calculateInSampleSize(
            srcWidth = srcWidth,
            srcHeight = srcHeight,
            dstWidth = dstWidth,
            dstHeight = dstHeight,
            scale = options.scale,
        )

        // Calculate the image's density scaling multiple.
        var scale = computeSizeMultiplier(
            srcWidth = srcWidth / inSampleSize,
            srcHeight = srcHeight / inSampleSize,
            dstWidth = dstWidth,
            dstHeight = dstHeight,
            scale = options.scale,
        )

        // Only upscale the image if the options require an exact size.
        if (options.allowInexactSize) {
            scale = scale.coerceAtMost(1.0)
        }

        inScaled = scale != 1.0
        if (inScaled) {
            if (scale > 1) {
                // Upscale
                inDensity = (Int.MAX_VALUE / scale).roundToInt()
                inTargetDensity = Int.MAX_VALUE
            } else {
                // Downscale
                inDensity = Int.MAX_VALUE
                inTargetDensity = (Int.MAX_VALUE * scale).roundToInt()
            }
        }
    }

    class Factory(
        private val context: Context? = null,
        maxParallelism: Int = Options.DEFAULT_MAX_PARALLELISM,
    ) : Decoder.Factory {

        private val parallelismLock = Semaphore(maxParallelism)

        override fun create(source: DecodeSource, options: Options): Decoder {
            return BitmapFactoryDecoder(
                context = context ?: options.androidContext,
                source = source,
                options = options,
                parallelismLock = parallelismLock,
            )
        }
    }

    /** Prevent [BitmapFactory.decodeStream] from swallowing [Exception]s. */
    private class ExceptionCatchingSource(delegate: Source) : ForwardingSource(delegate) {

        var exception: Exception? = null
            private set

        override fun read(sink: Buffer, byteCount: Long): Long {
            try {
                return super.read(sink, byteCount)
            } catch (e: Exception) {
                exception = e
                throw e
            }
        }
    }
}
