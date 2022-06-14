package com.seiko.imageloader.size

sealed class Dimension {
    data class Pixels(val px: Int) : Dimension()
    object Undefined : Dimension()
}

@Suppress("FunctionName")
fun Dimension(px: Int) = Dimension.Pixels(px)

inline fun Dimension.pxOrElse(block: () -> Int): Int {
    return if (this is Dimension.Pixels) px else block()
}

internal fun Dimension.toPx(scale: Scale) = pxOrElse {
    when (scale) {
        Scale.FILL -> Int.MIN_VALUE
        Scale.FIT -> Int.MAX_VALUE
    }
}
