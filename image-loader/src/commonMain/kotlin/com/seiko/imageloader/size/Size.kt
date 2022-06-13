package com.seiko.imageloader.size

data class Size(
    val width: Dimension,
    val height: Dimension,
) {
    companion object {
        val ORIGINAL = Size(
            width = Dimension.Undefined,
            height = Dimension.Undefined,
        )
    }
}

fun Size(width: Int, height: Int) = Size(Dimension(width), Dimension(height))

val Size.isOriginal: Boolean
    get() = this === Size.ORIGINAL

internal inline fun Size.widthPx(scale: Scale, original: () -> Int): Int {
    return if (isOriginal) original() else width.toPx(scale)
}

internal inline fun Size.heightPx(scale: Scale, original: () -> Int): Int {
    return if (isOriginal) original() else height.toPx(scale)
}
