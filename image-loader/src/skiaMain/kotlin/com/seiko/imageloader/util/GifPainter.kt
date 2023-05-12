package com.seiko.imageloader.util

import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.IntSize
import com.seiko.imageloader.option.Options
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Codec
import kotlin.time.Duration.Companion.milliseconds

internal class GifPainter(
    private val codec: Codec,
    private val imageScope: CoroutineScope,
    private val playAnimate: Boolean = true,
    private val repeatCount: Int = Options.REPEAT_INFINITE,
) : Painter(), RememberObserver {

    private var bitmapCache: Bitmap? = null
    private var drawImageBitmap = mutableStateOf<ImageBitmap?>(null)

    private var rememberJob: Job? = null

    override val intrinsicSize: Size
        get() = Size(codec.width.toFloat(), codec.height.toFloat())

    override fun onRemembered() {
        // Short circuit if we're already remembered.
        if (rememberJob != null) return

        rememberJob = imageScope.launch {
            when {
                codec.framesInfo.isEmpty() -> Unit
                codec.framesInfo.size == 1 || !playAnimate -> {
                    drawImageBitmap.value = getImageBitmap(codec, 0)
                }
                else -> {
                    var loopIteration = -1
                    while (repeatCount == Options.REPEAT_INFINITE || loopIteration++ < repeatCount) {
                        for ((index, frame) in codec.framesInfo.withIndex()) {
                            drawImageBitmap.value = getImageBitmap(codec, index)
                            delay(frame.duration.milliseconds)
                        }
                    }
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
        bitmapCache = null
        drawImageBitmap.value = null
    }

    override fun DrawScope.onDraw() {
        drawImageBitmap.value?.let {
            val intSize = IntSize(size.width.toInt(), size.height.toInt())
            drawImage(it, dstSize = intSize)
        }
    }

    private fun getImageBitmap(codec: Codec, frameIndex: Int): ImageBitmap {
        val bitmap = recycleBitmap(codec)
        codec.readPixels(bitmap, frameIndex)
        return bitmap.asComposeImageBitmap()
    }

    private fun recycleBitmap(codec: Codec): Bitmap {
        return bitmapCache?.let {
            if (codec.width == bitmapCache?.width && codec.height == bitmapCache?.height) {
                it.apply { allocPixels(codec.imageInfo) }
            } else {
                null
            }
        } ?: Bitmap().apply { allocPixels(codec.imageInfo) }
            .also {
                bitmapCache = it
            }
    }
}
