package com.seiko.imageloader.option

import androidx.compose.ui.geometry.Size
import kotlinx.coroutines.CompletableDeferred

class AsyncSizeResolver : SizeResolver {

    private val sizeObserver = CompletableDeferred<Size>()

    override suspend fun size(): Size {
        return sizeObserver.await()
    }

    fun setSize(size: Size) {
        sizeObserver.complete(size)
    }
}
