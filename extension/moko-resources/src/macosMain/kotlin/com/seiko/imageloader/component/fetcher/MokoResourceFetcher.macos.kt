package com.seiko.imageloader.component.fetcher

import androidx.compose.ui.graphics.Color
import com.seiko.imageloader.Image
import dev.icerock.moko.resources.AssetResource
import dev.icerock.moko.resources.ColorResource
import dev.icerock.moko.resources.FileResource
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.getNSColor
import okio.BufferedSource
import platform.AppKit.NSImage
import platform.CoreGraphics.CGImageRef
import platform.CoreImage.CIColor


internal actual fun AssetResource.toSource(): BufferedSource {
    TODO("Not yet implemented")
}

internal actual fun ColorResource.toColor(): Color {
    val color = CIColor(getNSColor().CGColor)
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
    val nsImage: NSImage = this.toNSImage()
        ?: throw IllegalArgumentException("can't read NSImage of $this")
    val cgImage: CGImageRef = nsImage.CGImageForProposedRect(
        proposedDestRect = null,
        context = null,
        hints = null
    ) ?: throw IllegalArgumentException("can't read CGImage of $this")
    return cgImage.toSkiaImage()
}
