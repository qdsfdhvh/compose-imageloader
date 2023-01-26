package com.seiko.imageloader.component

import android.content.Context
import com.seiko.imageloader.util.httpEngine
import io.ktor.client.HttpClient

fun ComponentRegistryBuilder.setupDefaultComponents(
    context: Context,
    maxImageSize: Int = 4096,
    httpClient: () -> HttpClient = { HttpClient(httpEngine) },
) {
    setupKtorComponents(httpClient)
    setupBase64Components()
    setupCommonComponents()
    setupJvmComponents()
    setupAndroidComponents(context, maxImageSize)
}
