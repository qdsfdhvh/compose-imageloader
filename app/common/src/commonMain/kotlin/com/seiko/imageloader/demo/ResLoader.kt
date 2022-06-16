package com.seiko.imageloader.demo

import androidx.compose.runtime.staticCompositionLocalOf

expect class ResLoader {
    // fun getString(file: AssetResource): String
}

val LocalResLoader = staticCompositionLocalOf<ResLoader> { error("null ResLoader") }
