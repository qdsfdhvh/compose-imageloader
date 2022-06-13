package com.seiko.imageloader.size

fun interface SizeResolver {
    suspend fun size(): Size
}
