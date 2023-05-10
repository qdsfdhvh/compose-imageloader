package com.seiko.imageloader.component.fetcher

import androidx.compose.ui.graphics.Color
import com.seiko.imageloader.Image
import dev.icerock.moko.resources.AssetResource
import dev.icerock.moko.resources.ColorResource
import dev.icerock.moko.resources.FileResource
import dev.icerock.moko.resources.ImageResource
import okio.BufferedSource

internal actual fun AssetResource.toSource(): BufferedSource {
    TODO("Not yet implemented")
}

internal actual fun ColorResource.toColor(): Color {
    TODO("Not yet implemented")
}

internal actual fun FileResource.toSource(): BufferedSource {
    TODO("Not yet implemented")
}

internal actual fun ImageResource.toImage(): Image {
    TODO("Not yet implemented")
}