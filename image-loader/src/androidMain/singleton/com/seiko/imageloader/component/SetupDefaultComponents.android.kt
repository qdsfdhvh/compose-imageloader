package com.seiko.imageloader.component

import android.content.Context
import androidx.compose.ui.unit.Density
import com.seiko.imageloader.component.decoder.BitmapFactoryDecoder
import com.seiko.imageloader.util.httpEngineFactory
import io.ktor.client.HttpClient

fun ComponentRegistryBuilder.setupDefaultComponents(
    context: Context? = null,
    density: Density? = context?.let { Density(it) },
    maxImageSize: Int = BitmapFactoryDecoder.DEFAULT_MAX_IMAGE_SIZE,
    maxParallelism: Int = BitmapFactoryDecoder.DEFAULT_MAX_PARALLELISM,
    httpClient: () -> HttpClient = httpEngineFactory,
) {
    setupKtorComponents(httpClient)
    setupBase64Components()
    setupCommonComponents()
    setupJvmComponents()
    setupAndroidComponents(
        context = context,
        density = density,
        maxImageSize = maxImageSize,
        maxParallelism = maxParallelism,
    )
}
