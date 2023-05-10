package com.seiko.imageloader.component.fetcher

import androidx.compose.ui.graphics.Color
import com.seiko.imageloader.Image
import dev.icerock.moko.resources.AssetResource
import dev.icerock.moko.resources.ColorResource
import dev.icerock.moko.resources.FileResource
import dev.icerock.moko.resources.ImageResource
import okio.BufferedSource
import okio.buffer
import okio.source
import org.jetbrains.skiko.toImage
import java.io.FileNotFoundException

internal actual fun AssetResource.toSource(): BufferedSource {
    return (this as FileResource).toSource()
}

internal actual fun ColorResource.toColor(): Color {
    return Color(
        red = lightColor.red,
        blue = lightColor.blue,
        green = lightColor.green,
        alpha = lightColor.alpha,
    )
}

internal actual fun FileResource.toSource(): BufferedSource {
    val stream = resourcesClassLoader.getResourceAsStream(filePath)
        ?: throw FileNotFoundException("Couldn't open resource as stream at: $filePath")
    return stream.source().buffer()
}

internal actual fun ImageResource.toImage(): Image {
    return image.toImage()
}
