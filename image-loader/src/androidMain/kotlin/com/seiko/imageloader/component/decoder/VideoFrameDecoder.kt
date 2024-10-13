package com.seiko.imageloader.component.decoder

import android.media.MediaMetadataRetriever
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.geometry.isSpecified
import com.seiko.imageloader.Bitmap
import com.seiko.imageloader.model.mimeType
import com.seiko.imageloader.model.videoFrameIndex
import com.seiko.imageloader.model.videoFrameMicros
import com.seiko.imageloader.model.videoFrameOption
import com.seiko.imageloader.model.videoFramePercent
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.util.calculateDstSize
import com.seiko.imageloader.util.getFrameAtIndexCompat
import com.seiko.imageloader.util.getFrameAtTimeCompat
import com.seiko.imageloader.util.getScaledFrameAtTimeCompat
import com.seiko.imageloader.util.toAndroidConfig
import com.seiko.imageloader.util.use
import okio.BufferedSource
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import java.io.File
import kotlin.math.roundToLong
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.M)
class VideoFrameDecoder(
    private val source: BufferedSource,
    private val options: Options,
) : Decoder {
    override suspend fun decode(): DecodeResult = MediaMetadataRetriever().use { retriever ->
        retriever.setDataSource(source.tempFile().path)

        // Resolve the dimensions to decode the video frame at accounting
        // for the source's aspect ratio and the target's size.
        val srcWidth: Int
        val srcHeight: Int
        val rotation = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)
            ?.toIntOrNull() ?: 0
        if (rotation == 90 || rotation == 270) {
            srcWidth = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
                ?.toIntOrNull() ?: 0
            srcHeight = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
                ?.toIntOrNull() ?: 0
        } else {
            srcWidth = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
                ?.toIntOrNull() ?: 0
            srcHeight = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
                ?.toIntOrNull() ?: 0
        }

        val maxImageSize = if (options.size.isSpecified && !options.size.isEmpty()) {
            minOf(options.size.width, options.size.height).toInt()
                .coerceAtMost(options.maxImageSize)
        } else {
            options.maxImageSize
        }
        val (dstWidth, dstHeight) = calculateDstSize(srcWidth, srcHeight, maxImageSize)

        val frameBitmap = retriever.getFrameBitmap(dstWidth, dstHeight)
            ?: error("Failed to decode video frame with index=${options.videoFrameIndex} or timeUs=${retriever.computeFrameMicros()}.")

        // TODO: scale frame bitmap

        return DecodeResult.OfBitmap(
            bitmap = frameBitmap,
        )
    }

    private fun MediaMetadataRetriever.getFrameBitmap(dstWidth: Int, dstHeight: Int): Bitmap? {
        return if (Build.VERSION.SDK_INT >= 28 && options.videoFrameIndex >= 0) {
            getFrameAtIndexCompat(
                frameIndex = options.videoFrameIndex,
                config = options.bitmapConfig.toAndroidConfig(),
            )
        } else if (Build.VERSION.SDK_INT >= 27 && dstWidth > 0 && dstHeight > 0) {
            getScaledFrameAtTimeCompat(
                timeUs = computeFrameMicros(),
                option = options.videoFrameOption,
                dstWidth = dstWidth,
                dstHeight = dstHeight,
                config = options.bitmapConfig.toAndroidConfig(),
            )
        } else {
            getFrameAtTimeCompat(
                timeUs = computeFrameMicros(),
                option = options.videoFrameOption,
                config = options.bitmapConfig.toAndroidConfig(),
            )
        }
    }

    private fun MediaMetadataRetriever.computeFrameMicros(): Long {
        val frameMicros = options.videoFrameMicros
        if (frameMicros >= 0) {
            return frameMicros
        }

        val framePercent = options.videoFramePercent
        if (framePercent >= 0) {
            val durationMillis = extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                ?.toLongOrNull() ?: 0L
            return 1000 * (framePercent * durationMillis).roundToLong()
        }

        return 0
    }

    @RequiresApi(Build.VERSION_CODES.M)
    class Factory : Decoder.Factory {
        override fun create(source: DecodeSource, options: Options): Decoder? {
            if (!isApplicable(source.extra.mimeType)) return null
            return VideoFrameDecoder(source.source, options)
        }

        private fun isApplicable(mimeType: String?): Boolean {
            return mimeType != null && mimeType.startsWith("video/")
        }
    }
}

// We can't get the resource size of the video via okio.source, so for now we can only create temp file instead of MediaDataSource.
@RequiresApi(Build.VERSION_CODES.M)
private fun BufferedSource.tempFile(): File {
    val tempFile = File.createTempFile("temp_${Random.nextLong()}", null)
    tempFile.deleteOnExit()

    FileSystem.SYSTEM.write(tempFile.toOkioPath()) {
        writeAll(this@tempFile)
    }

    return tempFile
}
