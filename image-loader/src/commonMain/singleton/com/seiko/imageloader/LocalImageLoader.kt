package com.seiko.imageloader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.ReadOnlyComposable

val LocalImageLoader = createImageLoaderProvidableCompositionLocal()

expect class ImageLoaderProvidableCompositionLocal {
    val current: ImageLoader
        @Composable
        @ReadOnlyComposable
        get

    infix fun provides(value: ImageLoader): ProvidedValue<*>
}

expect fun createImageLoaderProvidableCompositionLocal(): ImageLoaderProvidableCompositionLocal
