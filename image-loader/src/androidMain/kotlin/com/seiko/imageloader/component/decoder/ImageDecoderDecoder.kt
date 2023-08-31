package com.seiko.imageloader.component.decoder

import android.content.Context
import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.graphics.drawable.Drawable
import android.os.Build.VERSION.SDK_INT
import androidx.annotation.RequiresApi
import androidx.core.graphics.decodeDrawable
import com.seiko.imageloader.component.fetcher.AssetUriFetcher
import com.seiko.imageloader.component.fetcher.ContentUriFetcher
import com.seiko.imageloader.component.fetcher.ResourceUriFetcher
import com.seiko.imageloader.model.metadata
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.option.androidContext
import com.seiko.imageloader.toImage
import com.seiko.imageloader.util.FrameDelayRewritingSource
import com.seiko.imageloader.util.ScaleDrawable
import com.seiko.imageloader.util.isAnimatedHeif
import com.seiko.imageloader.util.isAnimatedWebP
import com.seiko.imageloader.util.isGif
import com.seiko.imageloader.util.isHardware
import com.seiko.imageloader.util.toBitmapConfig
import kotlinx.coroutines.runInterruptible
import okio.BufferedSource
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toOkioPath
import okio.buffer
import java.io.File
import java.nio.ByteBuffer

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
    private val source: DecodeSource,
    private val options: Options,
    private val enforceMinimumFrameDelay: Boolean = true,
) : Decoder {

    override suspend fun decode(): DecodeResult = runInterruptible {
        var imageDecoder: ImageDecoder? = null
        val wrapSource = WrapDecodeSource(source)
        val drawable = try {
            wrapSource.toImageDecoderSource().decodeDrawable { _, _ ->
                // Capture the image decoder to manually close it later.
                imageDecoder = this

                // Configure any other attributes.
                configureImageDecoderProperties()
            }
        } finally {
            imageDecoder?.close()
            wrapSource.close()
        }
        DecodeResult.Image(
            image = wrapDrawable(drawable).toImage(),
        )
    }

    private fun wrapBufferedSource(channel: BufferedSource): BufferedSource {
        return if (enforceMinimumFrameDelay && isGif(channel)) {
            // Wrap the source to rewrite its frame delay as it's read.
            val rewritingSource = FrameDelayRewritingSource(channel)
            rewritingSource.buffer()
        } else {
            channel
        }
    }

    private fun WrapDecodeSource.toImageDecoderSource(): ImageDecoder.Source {
        when (val metadata = extra.metadata) {
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
        }
        val source = wrapBufferedSource(source)
        return when {
            SDK_INT >= 31 -> ImageDecoder.createSource(source.readByteArray())
            SDK_INT == 30 -> ImageDecoder.createSource(ByteBuffer.wrap(source.readByteArray()))
            // https://issuetracker.google.com/issues/139371066
            else -> ImageDecoder.createSource(tempPath.toFile())
        }
    }

    private fun ImageDecoder.configureImageDecoderProperties() {
        val config = options.imageConfig.toBitmapConfig()
        allocator = if (config.isHardware) {
            ImageDecoder.ALLOCATOR_HARDWARE
        } else {
            ImageDecoder.ALLOCATOR_SOFTWARE
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

    private class WrapDecodeSource(private val decodeSource: DecodeSource) {
        val extra get() = decodeSource.extra
        val source get() = decodeSource.source

        private val fileSystem get() = FileSystem.SYSTEM
        private var _tempPath: Path? = null

        private fun createTempPath(): Path {
            val temp = File.createTempFile("tmp", null).toOkioPath()
            fileSystem.write(temp) {
                writeAll(source)
            }
            return temp
        }

        val tempPath: Path
            get() = _tempPath ?: createTempPath().also { _tempPath = it }

        fun close() {
            _tempPath?.let(fileSystem::delete)
        }
    }

    class Factory(
        private val context: Context? = null,
        private val enforceMinimumFrameDelay: Boolean = true,
    ) : Decoder.Factory {

        override suspend fun create(source: DecodeSource, options: Options): Decoder? {
            if (!options.playAnimate) return null
            if (!isApplicable(source.source)) return null
            return ImageDecoderDecoder(
                context = context ?: options.androidContext,
                source = source,
                options = options,
                enforceMinimumFrameDelay = enforceMinimumFrameDelay,
            )
        }

        private fun isApplicable(source: BufferedSource): Boolean {
            return isGif(source) || isAnimatedWebP(source) || (SDK_INT >= 30 && isAnimatedHeif(source))
        }
    }
}
