package com.seiko.imageloader.component.decoder

import android.content.Context
import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.graphics.drawable.Drawable
import android.os.Build.VERSION.SDK_INT
import androidx.annotation.RequiresApi
import androidx.core.graphics.decodeDrawable
import com.seiko.imageloader.component.fetcher.AssetMetadata
import com.seiko.imageloader.component.fetcher.ContentMetadata
import com.seiko.imageloader.component.fetcher.ResourceMetadata
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.request.SourceResult
import com.seiko.imageloader.util.FrameDelayRewritingSource
import com.seiko.imageloader.util.MovieDrawable.Companion.REPEAT_INFINITE
import com.seiko.imageloader.util.ScaleDrawable
import com.seiko.imageloader.util.isAnimatedHeif
import com.seiko.imageloader.util.isAnimatedWebP
import com.seiko.imageloader.util.isGif
import com.seiko.imageloader.util.isHardware
import com.seiko.imageloader.util.log
import com.seiko.imageloader.util.toBitmapConfig
import com.seiko.imageloader.util.toPainter
import okio.BufferedSource
import okio.buffer
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
class ImageDecoderDecoder @JvmOverloads constructor(
    private val context: Context,
    private val source: SourceResult,
    private val options: Options,
    private val enforceMinimumFrameDelay: Boolean = true
) : Decoder {

    override suspend fun decode(): DecoderResult {
        var imageDecoder: ImageDecoder? = null
        val drawable = try {
            source.toImageDecoderSource().decodeDrawable { info, _ ->
                // Capture the image decoder to manually close it later.
                imageDecoder = this

                // Configure any other attributes.
                configureImageDecoderProperties()
            }
        } finally {
            imageDecoder?.close()
        }
        return DecodePainterResult(wrapDrawable(drawable).toPainter())
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

    private suspend fun SourceResult.toImageDecoderSource(): ImageDecoder.Source {
        // val file = fileOrNull()
        // if (file != null) {
        //     return ImageDecoder.createSource(file.toFile())
        // }

        val metadata = metadata
        if (metadata is AssetMetadata) {
            return ImageDecoder.createSource(context.assets, metadata.fileName)
        }
        if (metadata is ContentMetadata) {
            return ImageDecoder.createSource(context.contentResolver, metadata.uri)
        }
        if (metadata is ResourceMetadata && metadata.packageName == context.packageName) {
            return ImageDecoder.createSource(context.resources, metadata.resId)
        }

        val source = wrapBufferedSource(channel)
        return when {
            SDK_INT >= 31 -> ImageDecoder.createSource(source.readByteArray())
            SDK_INT == 30 -> ImageDecoder.createSource(ByteBuffer.wrap(source.readByteArray()))
            // https://issuetracker.google.com/issues/139371066
            // else -> ImageDecoder.createSource(file().toFile())
            else -> ImageDecoder.createSource(source.readByteArray())
        }
    }

    private fun ImageDecoder.configureImageDecoderProperties() {
        val config = options.config.toBitmapConfig()
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
        log(tag = "ImageDecoder") { "is anime drawable" }

        baseDrawable.repeatCount = REPEAT_INFINITE

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

    class Factory @JvmOverloads constructor(
        private val context: Context,
        private val enforceMinimumFrameDelay: Boolean = true
    ) : Decoder.Factory {

        override suspend fun create(source: SourceResult, options: Options): Decoder? {
            if (!isApplicable(source.channel)) return null
            return ImageDecoderDecoder(context, source, options, enforceMinimumFrameDelay)
        }

        private fun isApplicable(source: BufferedSource): Boolean {
            return isGif(source) || isAnimatedWebP(source) || (SDK_INT >= 30 && isAnimatedHeif(source))
        }

        override fun equals(other: Any?) = other is Factory

        override fun hashCode() = javaClass.hashCode()
    }
}
