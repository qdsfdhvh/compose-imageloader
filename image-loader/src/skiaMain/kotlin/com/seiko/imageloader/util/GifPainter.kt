package com.seiko.imageloader.util

import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.IntSize
import com.seiko.imageloader.option.Options
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Codec

internal class GifPainter(
    private val codec: Codec,
    private val repeatCount: Int = Options.REPEAT_INFINITE,
) : Painter(), AnimationPainter, RememberObserver {

    private val durations = codec.framesInfo.map { it.duration }
    private val totalDuration = durations.sum()

    private var startTime = -1L
    private var frame by mutableStateOf(0)
    private var loopIteration = -1

    private var bitmapCache: Bitmap? = null
    private var intSizeCache: IntSize? = null

    override val intrinsicSize: Size
        get() = Size(codec.width.toFloat(), codec.height.toFloat())

    override fun DrawScope.onDraw() {
        bitmapCache?.let { bitmap ->
            codec.readPixels(bitmap, frame, frame - 1)
            drawImage(bitmap.asComposeImageBitmap(), dstSize = recycleIntSize(size))
        }
    }

    private fun recycleIntSize(size: Size): IntSize {
        var intSize = intSizeCache
        if (intSize == null ||
            size.width.compareTo(intSize.width) != 0 ||
            size.height.compareTo(intSize.height) != 0
        ) {
            intSize = IntSize(size.width.toInt(), size.height.toInt()).also {
                intSizeCache = it
            }
        }
        return intSize
    }

    override fun isPlay(): Boolean {
        return totalDuration > 0 && (repeatCount == Options.REPEAT_INFINITE || loopIteration++ < repeatCount)
    }

    override fun update(frameTimeMillis: Long) {
        if (startTime == -1L) {
            startTime = frameTimeMillis
        }
        frame = frameOf(time = (frameTimeMillis - startTime) % totalDuration)
    }

    // WARNING: it is not optimal
    private fun frameOf(time: Long): Int {
        var t = 0
        for (frame in durations.indices) {
            t += durations[frame]
            if (t >= time) return frame
        }
        error("Unexpected")
    }

    override fun onRemembered() {
        bitmapCache = Bitmap().apply {
            allocPixels(codec.imageInfo)
        }
    }

    override fun onAbandoned() {
        clear()
    }

    override fun onForgotten() {
        clear()
    }

    private fun clear() {
        startTime = -1
        frame = 0
        loopIteration = -1
        bitmapCache?.close()
        bitmapCache = null
        intSizeCache = null
    }
}
