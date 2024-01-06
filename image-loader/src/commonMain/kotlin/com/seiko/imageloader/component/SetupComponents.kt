package com.seiko.imageloader.component

import com.seiko.imageloader.component.fetcher.Base64Fetcher
import com.seiko.imageloader.component.fetcher.BitmapFetcher
import com.seiko.imageloader.component.fetcher.ByteArrayFetcher
import com.seiko.imageloader.component.mapper.StringUriMapper

fun ComponentRegistryBuilder.setupBase64Components() {
    add(Base64Fetcher.Factory())
}

fun ComponentRegistryBuilder.setupCommonComponents() {
    add(StringUriMapper())
    add(BitmapFetcher.Factory())
    add(ByteArrayFetcher.Factory())
}
