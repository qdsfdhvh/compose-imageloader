package com.seiko.imageloader.component

import io.ktor.client.HttpClient

// httpClient: () -> HttpClient = httpEngineFactory will be crash: Backend Internal error: Exception during IR lowering
expect fun ComponentRegistryBuilder.setupDefaultComponents()

expect fun ComponentRegistryBuilder.setupDefaultComponents(httpClient: () -> HttpClient)
