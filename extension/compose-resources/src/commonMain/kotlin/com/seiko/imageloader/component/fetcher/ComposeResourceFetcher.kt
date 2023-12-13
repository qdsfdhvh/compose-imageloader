@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
package com.seiko.imageloader.component.fetcher

import com.seiko.imageloader.option.Options
import okio.Buffer
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.ImageResource
import org.jetbrains.compose.resources.readResourceBytes
import org.jetbrains.compose.resources.getPathByEnvironment

class ComposeResourceFetcher {
    class Factory : Fetcher.Factory {
        override fun create(data: Any, options: Options): Fetcher? {
            if (data is ImageResource) {
                return ImageResourceFetcher(data)
            }
            return null
        }
    }

    private class ImageResourceFetcher(
        private val resource: ImageResource,
    ): Fetcher {
        @OptIn(ExperimentalResourceApi::class)
        override suspend fun fetch(): FetchResult {
            val path = resource.getPathByEnvironment()
            return FetchResult.OfSource(
                source = Buffer().apply {
                    write(readResourceBytes(path))
                }
            )
        }
    }
}
