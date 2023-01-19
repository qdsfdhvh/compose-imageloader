@file:Suppress("DEPRECATION")

package com.seiko.imageloader.component.decoder

import android.graphics.Bitmap
import android.graphics.Movie
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.model.SourceResult
import com.seiko.imageloader.util.FrameDelayRewritingSource
import com.seiko.imageloader.util.MovieDrawable
import com.seiko.imageloader.util.isGif
import com.seiko.imageloader.util.isHardware
import com.seiko.imageloader.util.toBitmapConfig
import com.seiko.imageloader.util.toPainter
import kotlinx.coroutines.runInterruptible
import okio.BufferedSource
import okio.buffer

/**
 * A [Decoder] that uses [Movie] to decode GIFs.
 *
 * NOTE: Prefer using [ImageDecoderDecoder] on API 28 and above.
 *
 * @param enforceMinimumFrameDelay If true, rewrite a GIF's frame delay to a default value if
 *  it is below a threshold. See https://github.com/coil-kt/coil/issues/540 for more info.
 */
internal class GifDecoder @JvmOverloads constructor(
    private val source: SourceResult,
    private val options: Options,
    private val enforceMinimumFrameDelay: Boolean = true
) : Decoder {

    override suspend fun decode() = runInterruptible {
        val bufferSource = source.channel
        val bufferedSource: BufferedSource = if (enforceMinimumFrameDelay) {
            FrameDelayRewritingSource(bufferSource).buffer()
        } else {
            bufferSource
        }
        val movie: Movie? = bufferedSource.use { Movie.decodeStream(it.inputStream()) }

        check(movie != null && movie.width() > 0 && movie.height() > 0) { "Failed to decode GIF." }

        val config = options.config.toBitmapConfig()
        val drawable = MovieDrawable(
            movie = movie,
            config = when {
                // movie.isOpaque && options.allowRgb565 -> Bitmap.Config.RGB_565
                config.isHardware -> Bitmap.Config.ARGB_8888
                else -> config
            },
            scale = options.scale
        )

        // drawable.setRepeatCount(options.parameters.repeatCount() ?: MovieDrawable.REPEAT_INFINITE)

        // Set the start and end animation callbacks if any one is supplied through the request.
        // val onStart = options.parameters.animationStartCallback()
        // val onEnd = options.parameters.animationEndCallback()
        // if (onStart != null || onEnd != null) {
        //     drawable.registerAnimationCallback(animatable2CompatCallbackOf(onStart, onEnd))
        // }

        // Set the animated transformation to be applied on each frame.
        // drawable.setAnimatedTransformation(options.parameters.animatedTransformation())

        DecodePainterResult(
            painter = drawable.toPainter(),
        )
    }

    class Factory @JvmOverloads constructor(
        private val enforceMinimumFrameDelay: Boolean = true
    ) : Decoder.Factory {

        override suspend fun create(source: SourceResult, options: Options): Decoder? {
            if (!isGif(source.channel)) return null
            return GifDecoder(source, options, enforceMinimumFrameDelay)
        }

        override fun equals(other: Any?) = other is Factory

        override fun hashCode() = javaClass.hashCode()
    }

    companion object {
        const val REPEAT_COUNT_KEY = "coil#repeat_count"
        const val ANIMATED_TRANSFORMATION_KEY = "coil#animated_transformation"
        const val ANIMATION_START_CALLBACK_KEY = "coil#animation_start_callback"
        const val ANIMATION_END_CALLBACK_KEY = "coil#animation_end_callback"
    }
}
