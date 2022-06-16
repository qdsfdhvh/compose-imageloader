package com.seiko.imageloader.util

import android.graphics.BitmapFactory
import androidx.annotation.Px
import com.seiko.imageloader.size.Scale
import io.ktor.utils.io.ByteReadChannel
import okio.ByteString.Companion.encodeUtf8
import kotlin.math.max
import kotlin.math.min

/** A collection of useful utility methods for decoding images. */
internal object DecodeUtils {

    private val SVG_TAG = "<svg".encodeUtf8()
    private val LEFT_ANGLE_BRACKET = "<".encodeUtf8()

    suspend fun isSvg(channel: ByteReadChannel): Boolean {
        return channel.rangeEquals(0, LEFT_ANGLE_BRACKET)
            // && channel.indexOf(SVG_TAG, 0, 1024) != -1L
    }

    // private fun BufferedSource.indexOf(bytes: ByteString, fromIndex: Long, toIndex: Long): Long {
    //     require(bytes.size > 0) { "bytes is empty" }
    //
    //     val firstByte = bytes[0]
    //     val lastIndex = toIndex - bytes.size
    //     var currentIndex = fromIndex
    //     while (currentIndex < lastIndex) {
    //         currentIndex = indexOf(firstByte, currentIndex, lastIndex)
    //         if (currentIndex == -1L || rangeEquals(currentIndex, bytes)) {
    //             return currentIndex
    //         }
    //         currentIndex++
    //     }
    //     return -1
    // }

    /**
     * Calculate the [BitmapFactory.Options.inSampleSize] given the source dimensions of the image
     * ([srcWidth] and [srcHeight]), the output dimensions ([dstWidth], [dstHeight]), and the [scale].
     */
    @JvmStatic
    fun calculateInSampleSize(
        @Px srcWidth: Int,
        @Px srcHeight: Int,
        @Px dstWidth: Int,
        @Px dstHeight: Int,
        scale: Scale
    ): Int {
        val widthInSampleSize = Integer.highestOneBit(srcWidth / dstWidth)
        val heightInSampleSize = Integer.highestOneBit(srcHeight / dstHeight)
        return when (scale) {
            Scale.FILL -> min(widthInSampleSize, heightInSampleSize)
            Scale.FIT -> max(widthInSampleSize, heightInSampleSize)
        }.coerceAtLeast(1)
    }

    /**
     * Calculate the percentage to multiply the source dimensions by to fit/fill the destination
     * dimensions while preserving aspect ratio.
     */
    @JvmStatic
    fun computeSizeMultiplier(
        @Px srcWidth: Int,
        @Px srcHeight: Int,
        @Px dstWidth: Int,
        @Px dstHeight: Int,
        scale: Scale
    ): Double {
        val widthPercent = dstWidth / srcWidth.toDouble()
        val heightPercent = dstHeight / srcHeight.toDouble()
        return when (scale) {
            Scale.FILL -> max(widthPercent, heightPercent)
            Scale.FIT -> min(widthPercent, heightPercent)
        }
    }

    /** @see computeSizeMultiplier */
    @JvmStatic
    fun computeSizeMultiplier(
        @Px srcWidth: Float,
        @Px srcHeight: Float,
        @Px dstWidth: Float,
        @Px dstHeight: Float,
        scale: Scale
    ): Float {
        val widthPercent = dstWidth / srcWidth
        val heightPercent = dstHeight / srcHeight
        return when (scale) {
            Scale.FILL -> max(widthPercent, heightPercent)
            Scale.FIT -> min(widthPercent, heightPercent)
        }
    }

    /** @see computeSizeMultiplier */
    @JvmStatic
    fun computeSizeMultiplier(
        @Px srcWidth: Double,
        @Px srcHeight: Double,
        @Px dstWidth: Double,
        @Px dstHeight: Double,
        scale: Scale
    ): Double {
        val widthPercent = dstWidth / srcWidth
        val heightPercent = dstHeight / srcHeight
        return when (scale) {
            Scale.FILL -> max(widthPercent, heightPercent)
            Scale.FIT -> min(widthPercent, heightPercent)
        }
    }
}
