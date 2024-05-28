package com.seiko.imageloader.util

import kotlinx.cinterop.ExperimentalForeignApi
import org.jetbrains.skia.Image
import platform.CoreGraphics.CGImageRef
import platform.UIKit.UIImage

@OptIn(ExperimentalForeignApi::class)
fun UIImage.toSkiaImage(): Image {
    val cgImage: CGImageRef = this.CGImage()
        ?: throw IllegalArgumentException("can't read CGImage of $this")
    return cgImage.toSkiaImage()
}
