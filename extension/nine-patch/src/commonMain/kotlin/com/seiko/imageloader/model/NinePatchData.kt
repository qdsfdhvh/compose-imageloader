package com.seiko.imageloader.model

import androidx.compose.ui.graphics.FilterQuality

data class NinePatchData internal constructor(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int,
    val scale: Float,
    val skipPadding: Int,
    val filterQuality: FilterQuality,
) {
    val width: Int get() = right - left
    val height: Int get() = bottom - top
}

class NinePatchDataBuilder internal constructor() {

    var left: Int = 0
        set(value) {
            check(value >= 0) { "left must >= 0" }
            field = value
        }

    var top: Int = 0
        set(value) {
            check(value >= 0) { "top must >= 0" }
            field = value
        }

    var right: Int = 0
        set(value) {
            check(value >= 0) { "right must >= 0" }
            field = value
        }

    var bottom: Int = 0
        set(value) {
            check(value >= 0) { "bottom must >= 0" }
            field = value
        }

    var scale: Float = 1f
        set(value) {
            check(value in 0f..1f) { "scale must in 0f..1f" }
            field = value
        }

    var skipPadding: Int = 0
        set(value) {
            check(value >= 0) { "skipPadding must >= 0" }
            field = value
        }

    var filterQuality: FilterQuality = FilterQuality.Medium

    fun build(): NinePatchData {
        check(left <= right) { "left must <= right" }
        check(top <= bottom) { "top must <= bottom" }
        return NinePatchData(
            left = left,
            top = top,
            right = right,
            bottom = bottom,
            scale = scale,
            skipPadding = skipPadding,
            filterQuality = filterQuality,
        )
    }
}

fun NinePatchData(block: NinePatchDataBuilder.() -> Unit): NinePatchData =
    NinePatchDataBuilder().apply(block).build()
