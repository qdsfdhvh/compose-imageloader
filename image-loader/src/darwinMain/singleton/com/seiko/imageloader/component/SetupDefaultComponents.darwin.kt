package com.seiko.imageloader.component

import androidx.compose.ui.unit.Density
import com.seiko.imageloader.util.httpEngine
import com.seiko.imageloader.util.ioDispatcher
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope

fun ComponentRegistryBuilder.setupDefaultComponents(
    imageScope: CoroutineScope = CoroutineScope(ioDispatcher),
    density: Density = Density(2f),
    httpClient: () -> HttpClient = { HttpClient(httpEngine) },
) {
    setupKtorComponents(httpClient)
    setupBase64Components()
    setupCommonComponents()
    setupSkiaComponents(imageScope, density)
}
