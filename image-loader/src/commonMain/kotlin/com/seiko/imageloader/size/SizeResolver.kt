package com.seiko.imageloader.size

import androidx.compose.ui.geometry.Size

interface SizeResolver {
    suspend fun size(): Size

    companion object {
        val Unspecified = SizeResolver(Size.Unspecified)
    }
}

fun SizeResolver(size: Size): SizeResolver = RealSizeResolver(size)

private class RealSizeResolver(private val size: Size) : SizeResolver {
    override suspend fun size() = size

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return other is RealSizeResolver && size == other.size
    }

    override fun hashCode() = size.hashCode()
}
