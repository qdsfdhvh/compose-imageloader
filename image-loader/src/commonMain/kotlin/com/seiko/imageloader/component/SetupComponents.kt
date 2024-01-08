package com.seiko.imageloader.component

import com.seiko.imageloader.component.fetcher.Base64Fetcher
import com.seiko.imageloader.component.fetcher.BitmapFetcher
import com.seiko.imageloader.component.fetcher.ByteArrayFetcher
import com.seiko.imageloader.component.fetcher.OkioPathFetcher
import com.seiko.imageloader.component.mapper.StringToUriMapper
import com.seiko.imageloader.component.mapper.UriToPathMapper
import com.seiko.imageloader.util.defaultFileSystem
import okio.FileSystem

fun ComponentRegistryBuilder.setupBase64Components() {
    add(Base64Fetcher.Factory())
}

fun ComponentRegistryBuilder.setupCommonComponents(fileSystem: FileSystem? = defaultFileSystem) {
    add(StringToUriMapper())
    add(UriToPathMapper())
    add(BitmapFetcher.Factory())
    add(ByteArrayFetcher.Factory())
    if (fileSystem != null) {
        add(OkioPathFetcher.Factory(fileSystem))
    }
}
