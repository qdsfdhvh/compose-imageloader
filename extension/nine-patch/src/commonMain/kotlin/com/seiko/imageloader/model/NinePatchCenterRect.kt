package com.seiko.imageloader.model

import androidx.compose.runtime.Immutable
import kotlin.math.max

@Immutable
class NinePatchCenterRect(
    left: Int,
    top: Int,
    right: Int,
    bottom: Int,
) {
    val left: Int = max(0, left)
    val top: Int = max(0, top)
    val right: Int = max(0, right)
    val bottom: Int = max(0, bottom)
    val width: Int get() = max(1, right - left)
    val height: Int get() = max(1, bottom - top)
    override fun toString(): String {
        return "NinePatchCenterRect{$left, $top, $right, $bottom}"
    }
}
