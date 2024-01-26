@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package com.seiko.imageloader.component.fetcher

import com.seiko.imageloader.option.Options
import okio.Buffer
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.getPathByEnvironment
import org.jetbrains.compose.resources.getSystemEnvironment
import org.jetbrains.compose.resources.readResourceBytes

@OptIn(ExperimentalResourceApi::class)
class ComposeResourceFetcher {
    class Factory : Fetcher.Factory {
        override fun create(data: Any, options: Options): Fetcher? {
            if (data is DrawableResource) {
                return DrawableResourceFetcher(data)
            }
            return null
        }
    }

    private class DrawableResourceFetcher(
        private val resource: DrawableResource,
    ) : Fetcher {
        @OptIn(InternalResourceApi::class)
        override suspend fun fetch(): FetchResult {
            val path = resource.getPathByEnvironment(getSystemEnvironment())
            return FetchResult.OfSource(
                source = Buffer().apply {
                    write(readResourceBytes(path))
                },
            )
        }
    }
}
