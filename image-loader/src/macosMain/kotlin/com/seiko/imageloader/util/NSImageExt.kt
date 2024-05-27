package com.seiko.imageloader.util

import kotlinx.cinterop.ExperimentalForeignApi
import org.jetbrains.skia.Image
import platform.AppKit.NSImage
import platform.CoreGraphics.CGImageRef

@OptIn(ExperimentalForeignApi::class)
fun NSImage.toSkiaImage(): Image {
    val cgImage: CGImageRef = this.CGImageForProposedRect(
        proposedDestRect = null,
        context = null,
        hints = null,
    ) ?: throw IllegalArgumentException("can't read CGImage of $this")
    return cgImage.toSkiaImage()
}
