package com.seiko.imageloader.component

import com.seiko.imageloader.component.fetcher.ByteBufferFetcher
import com.seiko.imageloader.component.fetcher.FileFetcher
import com.seiko.imageloader.component.keyer.FileKeyer
import com.seiko.imageloader.component.mapper.ByteArrayMapper
import com.seiko.imageloader.component.mapper.FileUriMapper

fun ComponentRegistryBuilder.setupJvmComponents() {
    // Mappers
    add(FileUriMapper())
    add(ByteArrayMapper())
    // Keyers
    add(FileKeyer())
    // Fetchers
    add(FileFetcher.Factory())
    add(ByteBufferFetcher.Factory())
}
