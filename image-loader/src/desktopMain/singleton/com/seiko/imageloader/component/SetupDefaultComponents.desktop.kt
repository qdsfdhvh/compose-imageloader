package com.seiko.imageloader.component

import androidx.compose.ui.unit.Density
import com.seiko.imageloader.util.defaultImageScope
import com.seiko.imageloader.util.httpEngineFactory
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope

fun ComponentRegistryBuilder.setupDefaultComponents(
    imageScope: CoroutineScope = defaultImageScope,
    density: Density = Density(2f),
    httpClient: () -> HttpClient = httpEngineFactory,
) {
    setupKtorComponents(httpClient)
    setupBase64Components()
    setupCommonComponents()
    setupJvmComponents()
    setupSkiaComponents(imageScope, density)
}
