package com.seiko.imageloader.util

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import com.google.accompanist.drawablepainter.DrawablePainter

fun Bitmap.toPainter(): BitmapPainter {
    return BitmapPainter(asImageBitmap())
}

fun Drawable.toPainter(): Painter {
    return when (this) {
        is BitmapDrawable -> bitmap.toPainter()
        is ColorDrawable -> ColorPainter(Color(color))
        else -> DrawablePainter(mutate())
    }
}
