package com.seiko.imageloader.option

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize

interface SizeResolver {
    suspend fun Density.size(): Size

    companion object {
        val Unspecified = SizeResolver(Size.Unspecified)
    }
}

fun SizeResolver(block: suspend () -> Size) = object : SizeResolver {
    override suspend fun Density.size(): Size = block()
}

fun SizeResolver(size: Size): SizeResolver = object : SizeResolver {
    override suspend fun Density.size(): Size = size
}

fun SizeResolver(size: DpSize): SizeResolver = object : SizeResolver {
    override suspend fun Density.size(): Size = size.toSize()
}
