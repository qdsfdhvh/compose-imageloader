package com.seiko.imageloader.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.WorkerThread
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import androidx.core.graphics.component3
import androidx.core.graphics.component4
import androidx.core.graphics.createBitmap
import com.seiko.imageloader.option.Scale
import kotlin.math.roundToInt

internal object DrawableUtils {

    private const val DEFAULT_SIZE = 512

    /**
     * Convert the provided [Drawable] into a [Bitmap].
     *
     * @param drawable The drawable to convert.
     * @param config The requested config for the bitmap.
     * @param size The requested size for the bitmap.
     * @param scale The requested scale for the bitmap.
     * @param allowInexactSize Allow returning a bitmap that is smaller than [size].
     */
    @WorkerThread
    fun convertToBitmap(
        drawable: Drawable,
        config: Bitmap.Config,
        scale: Scale,
        allowInexactSize: Boolean,
    ): Bitmap {
        // Fast path: return the underlying bitmap.
        if (drawable is BitmapDrawable) {
            val bitmap = drawable.bitmap
            if (isConfigValid(bitmap, config) && isSizeValid(
                    allowInexactSize,
                    bitmap,
                    scale,
                )
            ) {
                return bitmap
            }
        }

        // Slow path: draw the drawable on a new bitmap.
        val safeDrawable = drawable.mutate()
        val srcWidth = safeDrawable.width.let { if (it > 0) it else DEFAULT_SIZE }
        val srcHeight = safeDrawable.height.let { if (it > 0) it else DEFAULT_SIZE }
        val multiplier = DecodeUtils.computeSizeMultiplier(
            srcWidth = srcWidth,
            srcHeight = srcHeight,
            dstWidth = srcWidth,
            dstHeight = srcHeight,
            scale = scale,
        )
        val bitmapWidth = (multiplier * srcWidth).roundToInt()
        val bitmapHeight = (multiplier * srcHeight).roundToInt()

        val bitmap = createBitmap(bitmapWidth, bitmapHeight, config.toSoftware())
        safeDrawable.apply {
            val (oldLeft, oldTop, oldRight, oldBottom) = bounds
            setBounds(0, 0, bitmapWidth, bitmapHeight)
            draw(Canvas(bitmap))
            setBounds(oldLeft, oldTop, oldRight, oldBottom)
        }
        return bitmap
    }

    private fun isConfigValid(bitmap: Bitmap, config: Bitmap.Config): Boolean {
        return bitmap.config == config.toSoftware()
    }

    private fun isSizeValid(
        allowInexactSize: Boolean,
        bitmap: Bitmap,
        scale: Scale,
    ): Boolean {
        return if (allowInexactSize) {
            // Any size is valid.
            true
        } else {
            // The bitmap must match the scaled dimensions of the destination exactly.
            DecodeUtils.computeSizeMultiplier(
                srcWidth = bitmap.width,
                srcHeight = bitmap.height,
                dstWidth = bitmap.width,
                dstHeight = bitmap.height,
                scale = scale,
            ) == 1.0
        }
    }

    private val Drawable.width: Int
        get() = (this as? BitmapDrawable)?.bitmap?.width ?: intrinsicWidth

    private val Drawable.height: Int
        get() = (this as? BitmapDrawable)?.bitmap?.height ?: intrinsicHeight
}
