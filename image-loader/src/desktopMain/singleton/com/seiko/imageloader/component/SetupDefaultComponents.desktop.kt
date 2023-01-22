package com.seiko.imageloader.component

import androidx.compose.ui.unit.Density
import com.seiko.imageloader.util.httpEngine
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope

fun ComponentRegistryBuilder.setupDefaultComponents(
    imageScope: CoroutineScope,
    density: Density = Density(2f),
    httpClient: Lazy<HttpClient> = lazy { HttpClient(httpEngine) },
) {
    setupKtorComponents(httpClient)
    setupBase64Components()
    setupCommonComponents()
    setupJvmComponents()
    setupSkiaComponents(imageScope, density)
}
