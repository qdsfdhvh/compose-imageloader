package com.seiko.imageloader.component

import com.seiko.imageloader.component.fetcher.KtorUrlFetcher
import io.ktor.client.HttpClient

expect fun ComponentRegistryBuilder.setupDefaultComponents(
    httpClient: () -> HttpClient = KtorUrlFetcher.defaultHttpEngineFactory,
)
