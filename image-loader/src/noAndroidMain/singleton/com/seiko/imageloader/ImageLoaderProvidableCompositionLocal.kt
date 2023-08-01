package com.seiko.imageloader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.ReadOnlyComposable

actual class ImageLoaderProvidableCompositionLocal constructor(
    val delegate: ProvidableCompositionLocal<ImageLoader>,
) {
    actual inline val current: ImageLoader
        @Composable
        @ReadOnlyComposable
        get() = delegate.current

    actual infix fun provides(value: ImageLoader): ProvidedValue<*> {
        return delegate provides value
    }
}
