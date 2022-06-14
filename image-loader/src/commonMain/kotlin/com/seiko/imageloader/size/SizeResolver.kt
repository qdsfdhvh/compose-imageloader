package com.seiko.imageloader.size

interface SizeResolver {
    suspend fun size(): Size
}
