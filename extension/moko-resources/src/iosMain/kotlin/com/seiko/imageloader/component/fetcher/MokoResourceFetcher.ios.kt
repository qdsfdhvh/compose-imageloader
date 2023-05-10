package com.seiko.imageloader.component.fetcher

import androidx.compose.ui.graphics.Color
import com.seiko.imageloader.Image
import dev.icerock.moko.resources.AssetResource
import dev.icerock.moko.resources.ColorResource
import dev.icerock.moko.resources.FileResource
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.getUIColor
import okio.BufferedSource
import platform.CoreGraphics.CGImageRef
import platform.UIKit.UIImage

internal actual fun AssetResource.toSource(): BufferedSource {
    TODO("Not yet implemented")
}

internal actual fun ColorResource.toColor(): Color {
    val color = getUIColor().CIColor
    return Color(
        red = color.red.toFloat(),
        blue = color.blue.toFloat(),
        green = color.green.toFloat(),
        alpha = color.alpha.toFloat(),
    )
}

internal actual fun FileResource.toSource(): BufferedSource {
    TODO("Not yet implemented")
}

internal actual fun ImageResource.toImage(): Image {
    val uiImage: UIImage = this.toUIImage()
        ?: throw IllegalArgumentException("can't read UIImage of $this")
    val cgImage: CGImageRef = uiImage.CGImage()
        ?: throw IllegalArgumentException("can't read CGImage of $this")
    return cgImage.toSkiaImage()
}
