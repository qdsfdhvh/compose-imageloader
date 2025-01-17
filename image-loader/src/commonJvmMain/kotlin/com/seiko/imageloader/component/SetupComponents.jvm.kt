package com.seiko.imageloader.component

import com.seiko.imageloader.component.fetcher.ByteBufferFetcher
import com.seiko.imageloader.component.mapper.FileToPathMapper

fun ComponentRegistryBuilder.setupJvmComponents() {
    add(FileToPathMapper())
    add(ByteBufferFetcher.Factory())
}
