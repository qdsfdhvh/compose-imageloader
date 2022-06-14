package com.seiko.imageloader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

val LocalImageLoader = ImageLoaderProvidableCompositionLocal()

@JvmInline
value class ImageLoaderProvidableCompositionLocal internal constructor(
    private val delegate: ProvidableCompositionLocal<ImageLoader?> = staticCompositionLocalOf { null }
) {

    val current: ImageLoader
        @Composable
        @ReadOnlyComposable
        get() = delegate.current ?: error { "not provider imageLoader" }

    infix fun provides(value: ImageLoader) = delegate provides value
}