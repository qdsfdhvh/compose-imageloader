package com.seiko.imageloader.component.fetcher

import com.seiko.imageloader.model.ImageSourceFrom
import com.seiko.imageloader.model.toImageSource
import com.seiko.imageloader.option.Options
import okio.FileSystem
import okio.Path
import okio.buffer

class OkioPathFetcher private constructor(
    private val fileSystem: FileSystem,
    private val path: Path,
) : Fetcher {
    override suspend fun fetch(): FetchResult {
        return FetchResult.OfSource(
            imageSource = fileSystem.source(path).buffer().toImageSource(),
            imageSourceFrom = ImageSourceFrom.Disk,
        )
    }

    class Factory(
        private val fileSystem: FileSystem,
    ) : Fetcher.Factory {
        override fun create(data: Any, options: Options): Fetcher? {
            if (data !is Path) return null
            return OkioPathFetcher(fileSystem, data)
        }
    }
}
