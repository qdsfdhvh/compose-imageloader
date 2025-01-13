package com.seiko.imageloader.component.decoder

import android.content.Context
import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.annotation.RequiresApi
import androidx.core.graphics.decodeBitmap
import androidx.core.graphics.decodeDrawable
import com.seiko.imageloader.component.fetcher.AssetUriFetcher
import com.seiko.imageloader.component.fetcher.ByteBufferFetcher
import com.seiko.imageloader.component.fetcher.ContentUriFetcher
import com.seiko.imageloader.component.fetcher.ResourceUriFetcher
import com.seiko.imageloader.model.ImageSource
import com.seiko.imageloader.model.InputStreamImageSource
import com.seiko.imageloader.model.metadata
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.option.androidContext
import com.seiko.imageloader.toImage
import com.seiko.imageloader.util.DecodeUtils
import com.seiko.imageloader.util.ScaleDrawable
import com.seiko.imageloader.util.isHardware
import com.seiko.imageloader.util.tempFile
import com.seiko.imageloader.util.toAndroidConfig
import kotlinx.coroutines.runInterruptible
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlin.math.roundToInt

/**
 * A [Decoder] that uses [ImageDecoder] to decode GIFs, animated WebPs, and animated HEIFs.
 *
 * NOTE: Animated HEIF files are only supported on API 30 and above.
 *
 * @param enforceMinimumFrameDelay If true, rewrite a GIF's frame delay to a default value if
 *  it is below a threshold. See https://github.com/coil-kt/coil/issues/540 for more info.
 */
@RequiresApi(28)
class ImageDecoderDecoder private constructor(
    private val context: Context,
    private val imageSource: ImageSource,
    private val metadata: Any?,
    private val options: Options,
    private val parallelismLock: Semaphore,
    private val enforceMinimumFrameDelay: Boolean,
) : Decoder {

    private var isSampled = false

    override suspend fun decode(): DecodeResult = parallelismLock.withPermit {
        runInterruptible {
            var imageDecoder: ImageDecoder? = null
            isSampled = false
            try {
                val imageDecoderSource = imageSource.toImageDecoderSource()
                if (options.playAnimate && !options.isBitmap) {
                    val drawable = imageDecoderSource.decodeDrawable { info, _ ->
                        imageDecoder = this
                        configureImageDecoderProperties(info)
                    }
                    DecodeResult.OfImage(wrapDrawable(drawable).toImage())
                } else {
                    val bitmap = imageDecoderSource.decodeBitmap { info, _ ->
                        imageDecoder = this
                        configureImageDecoderProperties(info)
                    }
                    DecodeResult.OfBitmap(bitmap, isSampled = isSampled)
                }
            } finally {
                imageDecoder?.close()
                imageSource.close()
            }
        }
    }

    private fun ImageSource.toImageDecoderSource(): ImageDecoder.Source {
        when (metadata) {
            is AssetUriFetcher.MetaData -> {
                return ImageDecoder.createSource(context.assets, metadata.fileName)
            }
            is ContentUriFetcher.Metadata -> {
                return ImageDecoder.createSource(context.contentResolver, metadata.uri)
            }
            is ResourceUriFetcher.Metadata -> {
                if (metadata.packageName == context.packageName) {
                    return ImageDecoder.createSource(context.resources, metadata.resId)
                }
            }
            is ByteBufferFetcher.Metadata -> {
                return ImageDecoder.createSource(metadata.byteBuffer)
            }
        }
        return if (this is InputStreamImageSource) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ImageDecoder.createSource(bufferedSource.tempFile())
                // ImageDecoder.createSource(ByteBuffer.wrap(inputStream.readAllBytes()))
            } else {
                ImageDecoder.createSource(bufferedSource.tempFile())
            }
        } else {
            ImageDecoder.createSource(bufferedSource.tempFile())
        }
    }

    private fun ImageDecoder.configureImageDecoderProperties(
        info: ImageDecoder.ImageInfo,
    ) {
        val config = options.bitmapConfig.toAndroidConfig()
        allocator = if (config.isHardware) {
            ImageDecoder.ALLOCATOR_HARDWARE
        } else {
            ImageDecoder.ALLOCATOR_SOFTWARE
        }

        // Configure the output image's size.
        val srcWidth = info.size.width
        val srcHeight = info.size.height
        val (dstWidth, dstHeight) = DecodeUtils.computeDstSize(
            srcWidth = srcWidth,
            srcHeight = srcHeight,
            targetSize = options.size,
            scale = options.scale,
            maxSize = options.maxImageSize,
        )
        if (srcWidth > 0 && srcHeight > 0 &&
            (srcWidth != dstWidth || srcHeight != dstHeight)
        ) {
            val multiplier = DecodeUtils.computeSizeMultiplier(
                srcWidth = srcWidth,
                srcHeight = srcHeight,
                dstWidth = dstWidth,
                dstHeight = dstHeight,
                scale = options.scale,
            )

            // Set the target size if the image is larger than the requested dimensions
            // or the request requires exact dimensions.
            isSampled = multiplier < 1
            if (isSampled) {
                val targetWidth = (multiplier * srcWidth).roundToInt()
                val targetHeight = (multiplier * srcHeight).roundToInt()
                setTargetSize(targetWidth, targetHeight)
            }
        }

        // memorySizePolicy = if (options.allowRgb565) {
        //     ImageDecoder.MEMORY_POLICY_LOW_RAM
        // } else {
        //     ImageDecoder.MEMORY_POLICY_DEFAULT
        // }
        memorySizePolicy = ImageDecoder.MEMORY_POLICY_DEFAULT
        // if (options.colorSpace != null) {
        //     setTargetColorSpace(options.colorSpace)
        // }
        isUnpremultipliedRequired = !options.premultipliedAlpha
        // postProcessor = options.parameters.animatedTransformation()?.asPostProcessor()
    }

    private fun wrapDrawable(baseDrawable: Drawable): Drawable {
        if (baseDrawable !is AnimatedImageDrawable) {
            return baseDrawable
        }

        baseDrawable.repeatCount = options.repeatCount

        // Set the start and end animation callbacks if any one is supplied through the request.
        // val onStart = options.parameters.animationStartCallback()
        // val onEnd = options.parameters.animationEndCallback()
        // if (onStart != null || onEnd != null) {
        //     // Animation callbacks must be set on the main thread.
        //     withContext(Dispatchers.Main.immediate) {
        //         baseDrawable.registerAnimationCallback(animatable2CallbackOf(onStart, onEnd))
        //     }
        // }

        // Wrap AnimatedImageDrawable in a ScaleDrawable so it always scales to fill its bounds.
        return ScaleDrawable(baseDrawable, options.scale)
    }

    class Factory(
        private val context: Context? = null,
        maxParallelism: Int = Options.DEFAULT_MAX_PARALLELISM,
        // https://android.googlesource.com/platform/frameworks/base/+/2be87bb707e2c6d75f668c4aff6697b85fbf5b15
        private val enforceMinimumFrameDelay: Boolean = SDK_INT < 34,
    ) : Decoder.Factory {

        private val parallelismLock = Semaphore(maxParallelism)

        override fun create(source: DecodeSource, options: Options): Decoder {
            return ImageDecoderDecoder(
                context = context ?: options.androidContext,
                imageSource = source.imageSource,
                metadata = source.extra.metadata,
                options = options,
                parallelismLock = parallelismLock,
                enforceMinimumFrameDelay = enforceMinimumFrameDelay,
            )
        }
    }
}

// private fun InputStream.toByteBuffer(): ByteBuffer {
//     return ByteBuffer.wrap(readAllBytes())
//     // val byteBuffer = ByteBuffer.allocate(available())
//     // Channels.newChannel(this).read(byteBuffer)
//     // return byteBuffer
// }
