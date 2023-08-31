package com.seiko.imageloader.component

import androidx.compose.ui.unit.Density
import com.seiko.imageloader.util.DEFAULT_MAX_PARALLELISM
import com.seiko.imageloader.util.httpEngineFactory
import io.ktor.client.HttpClient

actual fun ComponentRegistryBuilder.setupDefaultComponents() {
    setupDefaultComponents(
        density = Density(2f),
    )
}

actual fun ComponentRegistryBuilder.setupDefaultComponents(httpClient: () -> HttpClient) {
    setupDefaultComponents(
        density = Density(2f),
        httpClient = httpClient,
    )
}

fun ComponentRegistryBuilder.setupDefaultComponents(
    density: Density = Density(2f),
    httpClient: () -> HttpClient = httpEngineFactory,
    maxParallelism: Int = DEFAULT_MAX_PARALLELISM,
) {
    setupKtorComponents(httpClient)
    setupBase64Components()
    setupCommonComponents()
    setupJvmComponents()
    setupSkiaComponents(density, maxParallelism)
}
