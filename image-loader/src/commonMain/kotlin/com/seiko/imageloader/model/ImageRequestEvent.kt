package com.seiko.imageloader.model

sealed interface ImageRequestEvent {
    object Prepare : ImageRequestEvent
    data class ReadMemoryCache(val hasCache: Boolean) : ImageRequestEvent
    data class ReadDiskCache(val hasCache: Boolean) : ImageRequestEvent
}

internal typealias ImageRequestEventListener = (ImageRequestEvent) -> Unit
