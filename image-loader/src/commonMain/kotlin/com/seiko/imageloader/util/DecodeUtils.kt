package com.seiko.imageloader.util

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.unit.IntSize
import com.seiko.imageloader.option.Scale
import kotlin.jvm.JvmStatic
import kotlin.math.roundToInt

/**
 * A collection of useful utility methods for decoding images.
 */
object DecodeUtils {

    /**
     * Calculate the `BitmapFactory.Options.inSampleSize` given the source dimensions of the image
     * ([srcWidth] and [srcHeight]), the output dimensions ([dstWidth], [dstHeight]), and the [scale].
     */
    @JvmStatic
    fun calculateInSampleSize(
        srcWidth: Int,
        srcHeight: Int,
        dstWidth: Int,
        dstHeight: Int,
        scale: Scale,
    ): Int {
        val widthInSampleSize = (srcWidth / dstWidth).takeHighestOneBit()
        val heightInSampleSize = (srcHeight / dstHeight).takeHighestOneBit()
        return when (scale) {
            Scale.FILL -> minOf(widthInSampleSize, heightInSampleSize)
            Scale.FIT -> maxOf(widthInSampleSize, heightInSampleSize)
        }.coerceAtLeast(1)
    }

    /**
     * Calculate the percentage to multiply the source dimensions by to fit/fill the destination
     * dimensions while preserving aspect ratio.
     */
    @JvmStatic
    fun computeSizeMultiplier(
        srcWidth: Int,
        srcHeight: Int,
        dstWidth: Int,
        dstHeight: Int,
        scale: Scale,
    ): Double {
        val widthPercent = dstWidth.toDouble() / srcWidth
        val heightPercent = dstHeight.toDouble() / srcHeight
        return when (scale) {
            Scale.FILL -> maxOf(widthPercent, heightPercent)
            Scale.FIT -> minOf(widthPercent, heightPercent)
        }
    }

    /** @see computeSizeMultiplier */
    @JvmStatic
    fun computeSizeMultiplier(
        srcWidth: Double,
        srcHeight: Double,
        dstWidth: Double,
        dstHeight: Double,
        scale: Scale,
    ): Double {
        val widthPercent = dstWidth / srcWidth
        val heightPercent = dstHeight / srcHeight
        return when (scale) {
            Scale.FILL -> maxOf(widthPercent, heightPercent)
            Scale.FIT -> minOf(widthPercent, heightPercent)
        }
    }

    /**
     * Parse [targetSize] and return the destination dimensions that the source image should be
     * scaled into. The returned dimensions can be passed to [computeSizeMultiplier] to get the
     * final size multiplier.
     */
    @JvmStatic
    fun computeDstSize(
        srcWidth: Int,
        srcHeight: Int,
        targetSize: Size,
        scale: Scale,
        maxSize: Int,
    ): IntSize {
        val aspectRatio = srcWidth.toDouble() / srcHeight

        // val finalTargetSize: Size = if (targetSize.isSpecified) {
        //     targetSize
        // } else {
        //     Size.Zero
        // }

        when (scale) {
            Scale.FIT -> {
                if (aspectRatio > 1) {
                    val width = if (targetSize.isSpecified && !targetSize.isEmpty()) {
                        minOf(targetSize.width.roundToInt(), srcWidth)
                    } else {
                        srcWidth
                    }.coerceAtMost(maxSize)

                    val height = (width / aspectRatio).roundToInt()
                    return IntSize(width, height)
                } else {
                    val height = if (targetSize.isSpecified && !targetSize.isEmpty()) {
                        minOf(targetSize.height.roundToInt(), srcHeight)
                    } else {
                        srcHeight
                    }.coerceAtMost(maxSize)
                    val width = (height * aspectRatio).roundToInt()
                    return IntSize(width, height)
                }
            }
            Scale.FILL -> {
                if (aspectRatio > 1) {
                    var height = if (targetSize.isSpecified && !targetSize.isEmpty()) {
                        minOf(targetSize.height.roundToInt(), srcHeight)
                    } else {
                        srcHeight
                    }.coerceAtMost(maxSize)
                    val width = (height * aspectRatio).roundToInt()
                    if (width > maxSize) {
                        height = (maxSize / aspectRatio).roundToInt()
                        return IntSize(maxSize, height)
                    } else {
                        return IntSize(width, height)
                    }
                } else {
                    var width = if (targetSize.isSpecified && !targetSize.isEmpty()) {
                        minOf(targetSize.width.roundToInt(), srcWidth)
                    } else {
                        srcWidth
                    }.coerceAtMost(maxSize)
                    val height = (width / aspectRatio).roundToInt()
                    if (height > maxSize) {
                        width = (maxSize * aspectRatio).roundToInt()
                        return IntSize(width, maxSize)
                    } else {
                        return IntSize(width, height)
                    }
                }
            }
        }
    }
}
