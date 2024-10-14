@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package com.seiko.imageloader.component.fetcher

import com.seiko.imageloader.model.ImageSourceFrom
import com.seiko.imageloader.model.toImageSource
import com.seiko.imageloader.option.Options
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.getResourceItemByEnvironment
import org.jetbrains.compose.resources.getSystemEnvironment
import org.jetbrains.compose.resources.readResourceBytes

@OptIn(ExperimentalResourceApi::class, InternalResourceApi::class)
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
        override suspend fun fetch(): FetchResult {
            val path = resource.getResourceItemByEnvironment(getSystemEnvironment()).path
            return FetchResult.OfSource(
                imageSource = readResourceBytes(path).toImageSource(),
                imageSourceFrom = ImageSourceFrom.Disk,
            )
        }
    }
}
