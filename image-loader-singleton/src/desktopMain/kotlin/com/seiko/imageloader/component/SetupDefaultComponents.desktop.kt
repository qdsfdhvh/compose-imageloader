package com.seiko.imageloader.component

import androidx.compose.ui.unit.Density
import com.seiko.imageloader.component.fetcher.KtorUrlFetcher
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.util.defaultFileSystem
import io.ktor.client.HttpClient
import okio.FileSystem

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
    fileSystem: FileSystem? = defaultFileSystem,
) {
    setupKtorComponents(httpClient)
    setupBase64Components()
    setupJvmComponents()
    setupSvgComponents(density)
    setupSkiaComponents(maxParallelism)
    setupCommonComponents(fileSystem)
}
