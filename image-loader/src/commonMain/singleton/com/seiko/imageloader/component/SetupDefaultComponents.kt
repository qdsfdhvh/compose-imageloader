package com.seiko.imageloader.component

import com.seiko.imageloader.util.httpEngineFactory
import io.ktor.client.HttpClient

expect fun ComponentRegistryBuilder.setupDefaultComponents(
    httpClient: () -> HttpClient = httpEngineFactory,
)
