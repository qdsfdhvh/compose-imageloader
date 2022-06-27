package com.seiko.imageloader.util

import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Codec

internal class GifPainter(
    private val codec: Codec,
    private val imageScope: CoroutineScope,
) : Painter(), RememberObserver {

    private var frameIndex = mutableStateOf(0)
    private var bitmapCache: Bitmap? = null

    private var rememberJob: Job? = null

    override val intrinsicSize: Size
        get() = Size(codec.width.toFloat(), codec.height.toFloat())

    override fun onRemembered() {
        // Short circuit if we're already remembered.
        if (rememberJob != null) return

        rememberJob = imageScope.launch {
            while (isActive) {
                for ((index, frame) in codec.framesInfo.withIndex()) {
                    frameIndex.value = index
                    delay(frame.duration.toLong())
                }
            }
        }
    }

    override fun onAbandoned() {
        clear()
    }

    override fun onForgotten() {
        clear()
    }

    private fun clear() {
        if (rememberJob == null) return
        rememberJob?.cancel()
        rememberJob = null
    }

    override fun DrawScope.onDraw() {
        val bitmap = recycleBitmap(codec)
        codec.readPixels(bitmap, frameIndex.value)
        val intSize = IntSize(size.width.toInt(), size.height.toInt())
        drawImage(bitmap.asComposeImageBitmap(), dstSize = intSize)
    }

    private fun recycleBitmap(codec: Codec): Bitmap {
        return bitmapCache?.let {
            if (codec.width == bitmapCache?.width && codec.height == bitmapCache?.height) {
                it.apply { allocPixels(codec.imageInfo) }
            } else null
        } ?: Bitmap().apply { allocPixels(codec.imageInfo) }
            .also {
                bitmapCache = it
            }
    }
}
