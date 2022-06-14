package com.seiko.imageloader.demo

import androidx.compose.runtime.staticCompositionLocalOf
import dev.icerock.moko.resources.AssetResource

expect class ResLoader {
    fun getString(file: AssetResource): String
}

val LocalResLoader = staticCompositionLocalOf<ResLoader> { error("null ResLoader") }
