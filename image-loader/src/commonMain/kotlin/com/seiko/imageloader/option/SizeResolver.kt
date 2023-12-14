package com.seiko.imageloader.option

import androidx.compose.ui.geometry.Size

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
