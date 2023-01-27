package com.seiko.imageloader.intercept

import com.google.android.renderscript.Toolkit
import com.seiko.imageloader.Bitmap

internal actual fun blur(input: Bitmap, radius: Int): Bitmap {
    return Toolkit.blur(input, radius)
}
