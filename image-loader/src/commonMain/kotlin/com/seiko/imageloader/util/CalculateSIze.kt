package com.seiko.imageloader.util

import com.seiko.imageloader.option.Scale
import kotlin.math.max
import kotlin.math.min

internal fun calculateDstSize(
    srcWidth: Int,
    srcHeight: Int,
    maxImageSize: Int,
): Pair<Int, Int> {
    if (maxImageSize <= 0) {
        return srcWidth to srcHeight
    }
    var dstWidth = srcWidth
    var dstHeight = srcHeight
    if (max(dstWidth, dstHeight) > maxImageSize) {
        if (dstWidth >= dstHeight) {
            dstHeight = ((maxImageSize.toFloat() / srcWidth.toFloat()) * dstHeight).toInt()
            dstWidth = maxImageSize
        } else {
            dstWidth = ((maxImageSize.toFloat() / srcHeight.toFloat()) * dstWidth).toInt()
            dstHeight = maxImageSize
        }
    }
    return dstWidth to dstHeight
}

/**
 * Calculate the [BitmapFactory.Options.inSampleSize] given the source dimensions of the image
 * ([srcWidth] and [srcHeight]), the output dimensions ([dstWidth], [dstHeight]), and the [scale].
 */
internal fun calculateInSampleSize(
    srcWidth: Int,
    srcHeight: Int,
    dstWidth: Int,
    dstHeight: Int,
    scale: Scale,
): Int {
    val widthInSampleSize = (srcWidth / dstWidth).takeHighestOneBit()
    val heightInSampleSize = (srcHeight / dstHeight).takeHighestOneBit()
    return when (scale) {
        Scale.FILL -> min(widthInSampleSize, heightInSampleSize)
        else -> max(widthInSampleSize, heightInSampleSize)
    }.coerceAtLeast(1)
}

/**
 * Calculate the percentage to multiply the source dimensions by to fit/fill the destination
 * dimensions while preserving aspect ratio.
 */
internal fun computeSizeMultiplier(
    srcWidth: Int,
    srcHeight: Int,
    dstWidth: Int,
    dstHeight: Int,
    scale: Scale,
): Double {
    val widthPercent = dstWidth.toDouble() / srcWidth.toDouble()
    val heightPercent = dstHeight.toDouble() / srcHeight.toDouble()
    return when (scale) {
        Scale.FILL -> max(widthPercent, heightPercent)
        else -> min(widthPercent, heightPercent)
    }
}
