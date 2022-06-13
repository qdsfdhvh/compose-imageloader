package com.seiko.imageloader.component.fetcher

import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.request.Options

fun interface Fetcher {
    suspend fun fetch(): FetchResult?
    fun interface Factory {
        fun create(data: Any, options: Options, imageLoader: ImageLoader): Fetcher?
    }
}
