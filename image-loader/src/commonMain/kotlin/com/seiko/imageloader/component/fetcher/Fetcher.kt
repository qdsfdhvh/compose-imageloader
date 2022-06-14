package com.seiko.imageloader.component.fetcher

import com.seiko.imageloader.request.Options

interface Fetcher {
    suspend fun fetch(): FetchResult?
    fun interface Factory {
        fun create(data: Any, options: Options): Fetcher?
    }
}
