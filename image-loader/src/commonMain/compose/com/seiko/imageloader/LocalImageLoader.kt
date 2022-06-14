package com.seiko.imageloader

import androidx.compose.runtime.staticCompositionLocalOf

val LocalImageLoader = staticCompositionLocalOf<ImageLoader> {
    error { "not provider imageLoader" }
}
