package com.seiko.imageloader.component.fetcher

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import com.seiko.imageloader.option.Options
import dev.icerock.moko.resources.AssetResource
import dev.icerock.moko.resources.ColorResource
import dev.icerock.moko.resources.FileResource
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.getUIColor
import kotlinx.cinterop.DoubleVarOf
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.buffer
import platform.CoreGraphics.CGFloat
import platform.CoreGraphics.CGImageRef
import platform.UIKit.UIImage

internal actual suspend fun AssetResource.toFetchResult(options: Options): FetchResult? {
    return (this as FileResource).toFetchResult(options)
}

@OptIn(ExperimentalForeignApi::class)
internal actual suspend fun ColorResource.toFetchResult(options: Options): FetchResult? {
    val uiColor = getUIColor()
    val color = memScoped {
        val red: DoubleVarOf<CGFloat> = alloc()
        val green: DoubleVarOf<CGFloat> = alloc()
        val blue: DoubleVarOf<CGFloat> = alloc()
        val alpha: DoubleVarOf<CGFloat> = alloc()
        uiColor.getRed(
            red = red.ptr,
            green = green.ptr,
            blue = blue.ptr,
            alpha = alpha.ptr,
        )
        Color(
            red = red.value.toFloat(),
            green = green.value.toFloat(),
            blue = blue.value.toFloat(),
            alpha = alpha.value.toFloat(),
        )
    }
    return FetchResult.OfPainter(
        painter = ColorPainter(color),
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

@OptIn(ExperimentalForeignApi::class)
@Suppress("INVISIBLE_MEMBER")
internal actual suspend fun ImageResource.toFetchResult(options: Options): FetchResult? {
    val uiImage: UIImage = this.toUIImage()
        ?: throw IllegalArgumentException("can't read UIImage of $this")
    val cgImage: CGImageRef = uiImage.CGImage()
        ?: throw IllegalArgumentException("can't read CGImage of $this")
    return FetchResult.OfImage(
        image = cgImage.toSkiaImage(),
    )
}
