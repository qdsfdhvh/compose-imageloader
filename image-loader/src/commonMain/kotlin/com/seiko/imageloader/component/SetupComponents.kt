package com.seiko.imageloader.component

import com.seiko.imageloader.component.fetcher.Base64Fetcher
import com.seiko.imageloader.component.fetcher.BitmapFetcher
import com.seiko.imageloader.component.fetcher.KtorUrlFetcher
import com.seiko.imageloader.component.keyer.KtorUrlKeyer
import com.seiko.imageloader.component.mapper.Base64Mapper
import com.seiko.imageloader.component.mapper.KtorUrlMapper
import com.seiko.imageloader.component.mapper.StringUriMapper
import com.seiko.imageloader.util.httpEngineFactory
import io.ktor.client.HttpClient

fun ComponentRegistryBuilder.setupKtorComponents(
    httpClient: () -> HttpClient = httpEngineFactory,
) {
    add(KtorUrlMapper())
    add(KtorUrlKeyer())
    add(KtorUrlFetcher.Factory(httpClient))
}

fun ComponentRegistryBuilder.setupBase64Components() {
    add(Base64Mapper())
    add(Base64Fetcher.Factory())
}

fun ComponentRegistryBuilder.setupCommonComponents() {
    add(StringUriMapper())
    add(BitmapFetcher.Factory())
}
