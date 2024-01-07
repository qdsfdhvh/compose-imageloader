package com.seiko.imageloader.component

import androidx.compose.ui.unit.Density
import com.seiko.imageloader.component.fetcher.KtorUrlFetcher
import com.seiko.imageloader.option.Options
import io.ktor.client.HttpClient

actual fun ComponentRegistryBuilder.setupDefaultComponents(httpClient: () -> HttpClient) {
    setupDefaultComponents(
        density = null,
        httpClient = httpClient,
    )
}

fun ComponentRegistryBuilder.setupDefaultComponents(
    density: Density? = null,
    httpClient: () -> HttpClient = KtorUrlFetcher.defaultHttpEngineFactory,
    maxParallelism: Int = Options.DEFAULT_MAX_PARALLELISM,
) {
    setupKtorComponents(httpClient)
    setupBase64Components()
    setupCommonComponents()
    setupJvmComponents()
    setupSvgComponents(density)
    setupSkiaComponents(maxParallelism)
}
