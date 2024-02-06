package com.seiko.imageloader.component

import com.seiko.imageloader.component.fetcher.KtorUrlFetcher
import com.seiko.imageloader.component.keyer.KtorUrlKeyer
import com.seiko.imageloader.component.mapper.KtorUrlMapper
import io.ktor.client.HttpClient

fun ComponentRegistryBuilder.setupKtorComponents(
    httpClient: () -> HttpClient = KtorUrlFetcher.defaultHttpEngineFactory,
) {
    add(KtorUrlMapper())
    add(KtorUrlKeyer())
    add(KtorUrlFetcher.Factory(httpClient))
}
