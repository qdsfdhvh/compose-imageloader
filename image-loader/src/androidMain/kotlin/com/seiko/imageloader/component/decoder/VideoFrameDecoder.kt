package com.seiko.imageloader.component.decoder

import android.media.MediaDataSource
import android.media.MediaMetadataRetriever
import android.os.Build
import androidx.annotation.RequiresApi
import com.seiko.imageloader.Bitmap
import com.seiko.imageloader.model.ImageSource
import com.seiko.imageloader.model.InputStreamImageSource
import com.seiko.imageloader.model.mimeType
import com.seiko.imageloader.model.videoFrameIndex
import com.seiko.imageloader.model.videoFrameMicros
import com.seiko.imageloader.model.videoFrameOption
import com.seiko.imageloader.model.videoFramePercent
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.util.DecodeUtils
import com.seiko.imageloader.util.getFrameAtIndexCompat
import com.seiko.imageloader.util.getFrameAtTimeCompat
import com.seiko.imageloader.util.getScaledFrameAtTimeCompat
import com.seiko.imageloader.util.tempFile
import com.seiko.imageloader.util.toAndroidConfig
import com.seiko.imageloader.util.use
import java.io.InputStream
import kotlin.math.roundToInt
import kotlin.math.roundToLong

@RequiresApi(Build.VERSION_CODES.M)
class VideoFrameDecoder(
    private val source: ImageSource,
    private val options: Options,
) : Decoder {
    override suspend fun decode(): DecodeResult = MediaMetadataRetriever().use { retriever ->
        retriever.setDataSource(source)

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

        val (dstWidth, dstHeight) = if (srcWidth > 0 && srcHeight > 0) {
            val (dstWidth, dstHeight) = DecodeUtils.computeDstSize(
                srcWidth = srcWidth,
                srcHeight = srcHeight,
                targetSize = options.size,
                scale = options.scale,
                maxSize = options.maxImageSize,
            )
            val rawScale = DecodeUtils.computeSizeMultiplier(
                srcWidth = srcWidth,
                srcHeight = srcHeight,
                dstWidth = dstWidth,
                dstHeight = dstHeight,
                scale = options.scale,
            )
            val scale = rawScale.coerceAtMost(1.0)

            val width = (scale * srcWidth).roundToInt()
            val height = (scale * srcHeight).roundToInt()
            width to height
        } else {
            // We were unable to decode the video's dimensions.
            // Fall back to decoding the video frame at the original size.
            // We'll scale the resulting bitmap after decoding if necessary.
            srcWidth to srcHeight
        }

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
            return VideoFrameDecoder(source.imageSource, options)
        }

        private fun isApplicable(mimeType: String?): Boolean {
            return mimeType != null && mimeType.startsWith("video/")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.M)
private fun MediaMetadataRetriever.setDataSource(source: ImageSource) {
    when (source) {
        is InputStreamImageSource -> {
            // TODO: without temp file
            setDataSource(source.bufferedSource.tempFile().path)
        }
        else -> {
            setDataSource(source.bufferedSource.tempFile().path)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.M)
private fun InputStream.toMediaDataSource(): MediaDataSource {
    return object : MediaDataSource() {
        override fun readAt(position: Long, buffer: ByteArray, offset: Int, size: Int): Int {
            return read(buffer, offset, size)
        }

        override fun getSize(): Long {
            return available().toLong()
        }

        override fun close() {
            this@toMediaDataSource.close()
        }
    }
}
