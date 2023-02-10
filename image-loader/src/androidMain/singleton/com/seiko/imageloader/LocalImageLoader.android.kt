package com.seiko.imageloader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext

actual class ImageLoaderProvidableCompositionLocal constructor(
    val delegate: ProvidableCompositionLocal<ImageLoader?>,
) {
    actual inline val current: ImageLoader
        @Composable
        @ReadOnlyComposable
        get() = delegate.current ?: LocalContext.current.imageLoader

    actual infix fun provides(value: ImageLoader): ProvidedValue<*> {
        return delegate provides value
    }
}

actual fun createImageLoaderProvidableCompositionLocal() = ImageLoaderProvidableCompositionLocal(
    delegate = staticCompositionLocalOf { null },
)
