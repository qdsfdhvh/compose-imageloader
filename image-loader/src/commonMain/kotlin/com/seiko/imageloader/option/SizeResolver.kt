package com.seiko.imageloader.option

import androidx.compose.ui.geometry.Size
import kotlinx.coroutines.CompletableDeferred

interface SizeResolver {
    suspend fun size(): Size

    companion object {
        val Unspecified: SizeResolver = RealSizeResolver(Size.Unspecified)
    }
}

fun SizeResolver(block: suspend () -> Size) = object : SizeResolver {
    override suspend fun size(): Size = block()
}

class AsyncSizeResolver : SizeResolver {

    private val sizeObserver = CompletableDeferred<Size>()

    override suspend fun size(): Size {
        return sizeObserver.await()
    }

    fun setSize(size: Size) {
        sizeObserver.complete(size)
    }
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
