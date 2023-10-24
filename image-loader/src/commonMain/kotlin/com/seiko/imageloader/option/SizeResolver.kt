package com.seiko.imageloader.option

import androidx.compose.ui.geometry.Size
import kotlinx.coroutines.CompletableDeferred

interface SizeResolver {
    suspend fun size(): Size

    companion object {
        val Unspecified = SizeResolver(Size.Unspecified)
    }
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

fun SizeResolver(block: suspend () -> Size) = object : SizeResolver {
    override suspend fun size(): Size = block()
}

fun SizeResolver(size: Size): SizeResolver = object : SizeResolver {
    override suspend fun size(): Size = size
}
