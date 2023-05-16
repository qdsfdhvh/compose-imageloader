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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.yield
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
    private var intSizeCache: IntSize? = null

    private var drawImageBitmap = mutableStateOf<ImageBitmap?>(null)

    private var rememberJob: Job? = null

    override val intrinsicSize: Size
        get() = Size(codec.width.toFloat(), codec.height.toFloat())

    override fun onRemembered() {
        // Short circuit if we're already remembered.
        if (rememberJob != null) return

        rememberJob = gifImageFlow()
            .onEach { drawImageBitmap.value = it }
            .launchIn(imageScope)
    }

    private fun gifImageFlow() = flow {
        yield()
        when {
            codec.framesInfo.isEmpty() -> Unit
            codec.framesInfo.size == 1 || !playAnimate -> {
                emit(getImageBitmap(codec, 0))
            }
            else -> {
                var loopIteration = -1
                while (repeatCount == Options.REPEAT_INFINITE || loopIteration++ < repeatCount) {
                    for ((index, frame) in codec.framesInfo.withIndex()) {
                        emit(getImageBitmap(codec, index))
                        delay(frame.duration.milliseconds)
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
        drawImageBitmap.value = null
        bitmapCache?.close()
        bitmapCache = null
        intSizeCache = null
    }

    override fun DrawScope.onDraw() {
        drawImageBitmap.value?.let { image ->
            drawImage(image, dstSize = recycleIntSize(size))
        }
    }

    private fun recycleIntSize(size: Size): IntSize {
        var intSize = intSizeCache
        if (intSize == null
            || size.width.compareTo(intSize.width) != 0
            || size.height.compareTo(intSize.height) != 0
        ) {
            intSize = IntSize(size.width.toInt(), size.height.toInt()).also {
                intSizeCache = it
            }
        }
        return intSize
    }

    private fun getImageBitmap(codec: Codec, frameIndex: Int): ImageBitmap {
        return recycleBitmap(codec).apply {
            codec.readPixels(this, frameIndex, frameIndex - 1)
        }.asComposeImageBitmap().also {
            it.prepareToDraw()
        }
    }

    private fun recycleBitmap(codec: Codec): Bitmap {
        val bitmap = bitmapCache ?: Bitmap().also {
            it.allocPixels(codec.imageInfo)
            bitmapCache = it
        }
        if (bitmap.width != codec.width && bitmap.height != codec.height) {
            bitmap.allocPixels(codec.imageInfo)
        }
        return bitmap
    }
}
