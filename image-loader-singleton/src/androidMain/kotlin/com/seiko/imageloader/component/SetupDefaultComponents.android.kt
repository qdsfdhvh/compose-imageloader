package com.seiko.imageloader.component

import android.content.Context
import androidx.compose.ui.unit.Density
import com.seiko.imageloader.component.fetcher.KtorUrlFetcher
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.util.defaultFileSystem
import io.ktor.client.HttpClient
import okio.FileSystem

actual fun ComponentRegistryBuilder.setupDefaultComponents(httpClient: () -> HttpClient) {
    setupDefaultComponents(
        context = null,
        density = null,
        httpClient = httpClient,
    )
}

fun ComponentRegistryBuilder.setupDefaultComponents(
    context: Context? = null,
    density: Density? = context?.let { Density(it) },
    httpClient: () -> HttpClient = KtorUrlFetcher.defaultHttpEngineFactory,
    maxParallelism: Int = Options.DEFAULT_MAX_PARALLELISM,
    fileSystem: FileSystem? = defaultFileSystem,
) {
    setupKtorComponents(httpClient)
    setupBase64Components()
    setupCommonComponents(fileSystem)
    setupSvgComponents(density)
    setupJvmComponents()
    setupAndroidComponents(context, maxParallelism)
}
