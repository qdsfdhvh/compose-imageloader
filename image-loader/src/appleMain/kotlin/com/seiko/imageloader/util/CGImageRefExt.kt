package com.seiko.imageloader.util

import cnames.structs.CGImage
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.Image
import org.jetbrains.skia.ImageInfo
import platform.CoreGraphics.CGBitmapContextCreate
import platform.CoreGraphics.CGColorSpaceCreateDeviceRGB
import platform.CoreGraphics.CGColorSpaceRef
import platform.CoreGraphics.CGColorSpaceRelease
import platform.CoreGraphics.CGContextClearRect
import platform.CoreGraphics.CGContextDrawImage
import platform.CoreGraphics.CGContextRef
import platform.CoreGraphics.CGContextRelease
import platform.CoreGraphics.CGImageGetHeight
import platform.CoreGraphics.CGImageGetWidth
import platform.CoreGraphics.CGImageRef
import platform.CoreGraphics.CGRect
import platform.CoreGraphics.CGRectMake
import platform.posix.size_t
import platform.posix.uint32_t

@OptIn(ExperimentalForeignApi::class)
fun CGImageRef.toSkiaImage(): Image {
    val cgImage: CPointer<CGImage> = this
    val width: size_t = CGImageGetWidth(cgImage)
    val height: size_t = CGImageGetHeight(cgImage)
    val space: CGColorSpaceRef? = CGColorSpaceCreateDeviceRGB()
    val bitsPerComponent: ULong = 8u
    val bitsPerPixel: ULong = bitsPerComponent * 4u
    val bytesPerPixel: ULong = bitsPerPixel / bitsPerComponent
    val bytesPerRow: ULong = width * bytesPerPixel
    val bufferSize: ULong = width * height * bytesPerPixel
    val bitmapInfo: uint32_t = platform.CoreGraphics.CGImageAlphaInfo.kCGImageAlphaPremultipliedLast.value

    check(bufferSize != 0UL) { "image can't be 0 bytes" }
    check(width > 0UL) { "width should be more then 0 px ($width)" }
    check(height > 0UL) { "height should be more then 0 px ($height)" }

    val rect: CValue<CGRect> = CGRectMake(
        x = 0.0,
        y = 0.0,
        width = width.toDouble(),
        height = height.toDouble(),
    )

    val bytes = ByteArray(bufferSize.toInt())

    bytes.usePinned { pinnedArray ->
        val data = pinnedArray.addressOf(0)

        val ctx: CGContextRef = CGBitmapContextCreate(
            data = data,
            width = width,
            height = height,
            bitsPerComponent = bitsPerComponent,
            bytesPerRow = bytesPerRow,
            space = space,
            bitmapInfo = bitmapInfo,
        ) ?: throw IllegalArgumentException("can't create bitmap context for $cgImage")

        CGContextClearRect(c = ctx, rect = rect)
        CGContextDrawImage(c = ctx, rect = rect, image = cgImage)

        CGContextRelease(ctx)

        if (space != null) {
            CGColorSpaceRelease(space)
        }

        return Image.makeRaster(
            imageInfo = ImageInfo(
                width = width.toInt(),
                height = height.toInt(),
                colorType = ColorType.RGBA_8888,
                alphaType = ColorAlphaType.PREMUL,
            ),
            bytes = bytes,
            rowBytes = bytesPerRow.toInt(),
        )
    }
}
