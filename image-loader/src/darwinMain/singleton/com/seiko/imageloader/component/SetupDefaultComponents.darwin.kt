package com.seiko.imageloader.component

import androidx.compose.ui.unit.Density
import com.seiko.imageloader.util.httpEngine
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

fun ComponentRegistryBuilder.setupDefaultComponents(
    imageScope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    density: Density = Density(2f),
    httpClient: () -> HttpClient = { HttpClient(httpEngine) },
) {
    setupKtorComponents(httpClient)
    setupBase64Components()
    setupCommonComponents()
    setupSkiaComponents(imageScope, density)
}
