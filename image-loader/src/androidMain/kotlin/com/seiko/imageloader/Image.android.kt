package com.seiko.imageloader

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import com.google.accompanist.drawablepainter.DrawablePainter

actual class Image(
    val drawable: Drawable,
)

@Suppress("NOTHING_TO_INLINE")
inline fun Drawable.toImage() = Image(this)

actual fun Image.toPainter(filterQuality: FilterQuality): Painter {
    return when (drawable) {
        is BitmapDrawable -> drawable.bitmap.toPainter(filterQuality)
        is ColorDrawable -> ColorPainter(Color(drawable.color))
        else -> DrawablePainter(drawable.mutate())
    }
}
