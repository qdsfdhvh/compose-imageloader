package com.seiko.imageloader.component.fetcher

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.util.toSkiaImage
import dev.icerock.moko.resources.AssetResource
import dev.icerock.moko.resources.ColorResource
import dev.icerock.moko.resources.FileResource
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.getNSColor
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.buffer
import platform.AppKit.NSColorSpace.Companion.deviceRGBColorSpace
import platform.AppKit.NSImage

internal actual suspend fun AssetResource.toFetchResult(options: Options): FetchResult? {
    return (this as FileResource).toFetchResult(options)
}

internal actual suspend fun ColorResource.toFetchResult(options: Options): FetchResult? {
    val nsColor = getNSColor()
    val deviceColor = nsColor.colorUsingColorSpace(deviceRGBColorSpace)
        ?: error("can't convert $nsColor to deviceRGBColorSpace")
    return FetchResult.OfPainter(
        painter = ColorPainter(
            Color(
                red = deviceColor.redComponent.toFloat(),
                green = deviceColor.greenComponent.toFloat(),
                blue = deviceColor.blueComponent.toFloat(),
                alpha = deviceColor.alphaComponent.toFloat(),
            ),
        ),
    )
}

internal actual suspend fun FileResource.toFetchResult(options: Options): FetchResult? {
    val path = bundle.pathForResource(
        name = fileName,
        ofType = extension,
        inDirectory = "files",
    )!!.toPath()
    return FetchResult.OfSource(
        source = FileSystem.SYSTEM.source(path).buffer(),
    )
}

@Suppress("INVISIBLE_MEMBER")
internal actual suspend fun ImageResource.toFetchResult(options: Options): FetchResult? {
    val nsImage: NSImage = this.toNSImage()
        ?: throw IllegalArgumentException("can't read NSImage of $this")
    return FetchResult.OfImage(
        image = nsImage.toSkiaImage(),
    )
}
