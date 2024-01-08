package com.seiko.imageloader.component

import com.seiko.imageloader.component.fetcher.ByteBufferFetcher
import com.seiko.imageloader.component.fetcher.FileFetcher
import com.seiko.imageloader.component.keyer.FileKeyer
import com.seiko.imageloader.component.mapper.StringToFileMapper

fun ComponentRegistryBuilder.setupJvmComponents() {
    add(StringToFileMapper())
    add(FileKeyer())
    add(FileFetcher.Factory())
    add(ByteBufferFetcher.Factory())
}
