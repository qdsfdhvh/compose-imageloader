package com.seiko.imageloader.component.fetcher

import com.seiko.imageloader.option.Options
import dev.icerock.moko.resources.AssetResource
import dev.icerock.moko.resources.ColorResource
import dev.icerock.moko.resources.FileResource
import dev.icerock.moko.resources.ImageResource

class MokoResourceFetcher {

    class Factory : Fetcher.Factory {
        override fun create(data: Any, options: Options): Fetcher? {
            return when (data) {
                is AssetResource -> AssetFetcher(data, options)
                is ColorResource -> ColorFetcher(data, options)
                is FileResource -> FileFetcher(data, options)
                is ImageResource -> ImageFetcher(data, options)
                else -> null
            }
        }
    }

    private class AssetFetcher(
        private val resource: AssetResource,
        private val options: Options,
    ) : Fetcher {
        override suspend fun fetch(): FetchResult? {
            return resource.toFetchResult(options)
        }
    }

    private class ColorFetcher(
        private val resource: ColorResource,
        private val options: Options,
    ) : Fetcher {
        override suspend fun fetch(): FetchResult? {
            return resource.toFetchResult(options)
        }
    }

    private class FileFetcher(
        private val resource: FileResource,
        private val options: Options,
    ) : Fetcher {
        override suspend fun fetch(): FetchResult? {
            return resource.toFetchResult(options)
        }
    }

    private class ImageFetcher(
        private val resource: ImageResource,
        private val options: Options,
    ) : Fetcher {
        override suspend fun fetch(): FetchResult? {
            return resource.toFetchResult(options)
        }
    }
}

internal expect suspend fun AssetResource.toFetchResult(options: Options): FetchResult?

internal expect suspend fun ColorResource.toFetchResult(options: Options): FetchResult?

internal expect suspend fun FileResource.toFetchResult(options: Options): FetchResult?

internal expect suspend fun ImageResource.toFetchResult(options: Options): FetchResult?
