package com.seiko.imageloader.intercept

import com.seiko.imageloader.Bitmap
import org.jetbrains.skia.Canvas
import org.jetbrains.skia.FilterTileMode
import org.jetbrains.skia.Image
import org.jetbrains.skia.ImageFilter
import org.jetbrains.skia.Paint

internal actual fun blur(input: Bitmap, radius: Int): Bitmap {
    val result = Bitmap().apply {
        allocN32Pixels(input.width, input.height)
    }
    val blur = Paint().apply {
        imageFilter = ImageFilter.makeBlur(
            radius.toFloat(),
            radius.toFloat(),
            FilterTileMode.CLAMP,
        )
    }
    val canvas = Canvas(result)
    canvas.saveLayer(null, blur)
    canvas.drawImageRect(Image.makeFromBitmap(input), input.bounds.toRect())
    canvas.restore()
    canvas.readPixels(result, 0, 0)
    canvas.close()
    return result
}
