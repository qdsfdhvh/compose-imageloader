package com.seiko.imageloader

import androidx.compose.runtime.staticCompositionLocalOf

actual fun createImageLoaderProvidableCompositionLocal() = ImageLoaderProvidableCompositionLocal(
    delegate = staticCompositionLocalOf {
        // no disk cache in wasmJs
        ImageLoader.createDefault()
    },
)
