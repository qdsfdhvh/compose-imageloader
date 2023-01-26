package com.seiko.imageloader.component

import com.seiko.imageloader.component.fetcher.Base64Fetcher
import com.seiko.imageloader.component.fetcher.BitmapFetcher
import com.seiko.imageloader.component.fetcher.KtorUrlFetcher
import com.seiko.imageloader.component.keyer.KtorUrlKeyer
import com.seiko.imageloader.component.mapper.Base64Mapper
import com.seiko.imageloader.component.mapper.KtorUrlMapper
import com.seiko.imageloader.component.mapper.StringUriMapper
import com.seiko.imageloader.util.httpEngine
import io.ktor.client.HttpClient

fun ComponentRegistryBuilder.setupKtorComponents(
    httpClient: () -> HttpClient = { HttpClient(httpEngine) },
) {
    // Mappers
    add(KtorUrlMapper())
    // Keyers
    add(KtorUrlKeyer())
    // Fetchers
    add(KtorUrlFetcher.Factory(httpClient))
}

fun ComponentRegistryBuilder.setupBase64Components() {
    // Mappers
    add(Base64Mapper())
    // Fetchers
    add(Base64Fetcher.Factory())
}

fun ComponentRegistryBuilder.setupCommonComponents() {
    // Mappers
    add(StringUriMapper())
    // Fetchers
    add(BitmapFetcher.Factory())
}
