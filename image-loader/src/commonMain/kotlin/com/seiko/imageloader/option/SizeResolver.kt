package com.seiko.imageloader.option

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize

interface SizeResolver {
    suspend fun size(): Size

    companion object {
        val Unspecified: SizeResolver = RealSizeResolver(Size.Unspecified)
    }
}

fun SizeResolver(block: suspend () -> Size) = object : SizeResolver {
    override suspend fun size(): Size = block()
}

fun SizeResolver(size: Size): SizeResolver = RealSizeResolver(size)

fun SizeResolver(size: Float): SizeResolver = RealSizeResolver(Size(size, size))

fun SizeResolver(width: Float, height: Float): SizeResolver = RealSizeResolver(Size(width, height))

fun SizeResolver(size: Int): SizeResolver = RealSizeResolver(Size(size.toFloat(), size.toFloat()))

fun SizeResolver(width: Int, height: Int): SizeResolver = RealSizeResolver(Size(width.toFloat(), height.toFloat()))

fun SizeResolver(density: Density, size: DpSize): SizeResolver = with(density) { RealSizeResolver(size.toSize()) }

fun SizeResolver(density: Density, size: Dp): SizeResolver = with(density) { RealSizeResolver(Size(size.toPx(), size.toPx())) }

fun SizeResolver(density: Density, width: Dp, height: Dp): SizeResolver = with(density) { RealSizeResolver(Size(width.toPx(), height.toPx())) }

private class RealSizeResolver(private val size: Size) : SizeResolver {
    override suspend fun size(): Size = size

    override fun equals(other: Any?): Boolean {
        return other is RealSizeResolver && size == other.size
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + size.hashCode()
        return result
    }
}
