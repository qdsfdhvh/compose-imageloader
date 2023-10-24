package com.seiko.imageloader.model

import androidx.compose.runtime.Composable
import com.seiko.imageloader.EmptyPainter
import com.seiko.imageloader.cache.CachePolicy
import com.seiko.imageloader.component.decoder.Decoder
import com.seiko.imageloader.component.fetcher.Fetcher
import com.seiko.imageloader.component.keyer.Keyer
import com.seiko.imageloader.component.mapper.Mapper
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.option.Scale
import com.seiko.imageloader.option.takeFrom
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ImageRequestTest {

    @Test
    fun image_request_data_test() {
        assertEquals(ImageRequest {}.data, NullRequestData)
        assertEquals(ImageRequest(1).data, 1)
        assertEquals(ImageRequest("data").data, "data")
        assertEquals(ImageRequest(ImageRequest("data")).data, "data")
        assertEquals(ImageRequest(ImageRequest("data")) {}.data, "data")
    }

    @Test
    fun image_request_options_test() {
        val request = ImageRequest {
            options {
                allowInexactSize = true
                premultipliedAlpha = false
                retryIfDiskDecodeError = false
                imageConfig = Options.ImageConfig.ALPHA_8
                scale = Scale.FIT
                memoryCachePolicy = CachePolicy.DISABLED
                diskCachePolicy = CachePolicy.READ_ONLY
                repeatCount = 5
                maxImageSize = 100
                extra {
                    put("a", "aa")
                }
            }
        }
        val options = Options {
            takeFrom(request)
        }
        assertTrue(options.allowInexactSize)
        assertFalse(options.premultipliedAlpha)
        assertFalse(options.retryIfDiskDecodeError)
        assertEquals(options.imageConfig, Options.ImageConfig.ALPHA_8)
        assertEquals(options.scale, Scale.FIT)
        assertEquals(options.memoryCachePolicy, CachePolicy.DISABLED)
        assertEquals(options.diskCachePolicy, CachePolicy.READ_ONLY)
        assertEquals(options.repeatCount, 5)
        assertEquals(options.maxImageSize, 100)
        assertEquals(options.extra["a"], "aa")
    }

    @Test
    fun image_request_other_params_test() {
        val placeholderPainterFactory = @Composable { EmptyPainter }
        val errorPainterFactory = @Composable { EmptyPainter }
        val mapperFactory = Mapper { _, _ -> }
        val keyerFactory = Keyer { _, _, _ -> null }
        val fetcherFactory = Fetcher.Factory { _, _ ->
            Fetcher { null }
        }
        val decoderFactory = Decoder.Factory { _, _ ->
            Decoder { null }
        }
        val request = ImageRequest {
            extra {
                put("a", "aaa")
            }
            placeholderPainter(placeholderPainterFactory)
            errorPainter(errorPainterFactory)
            skipEvent = true
            components {
                add(mapperFactory)
                add(keyerFactory)
                add(fetcherFactory)
                add(decoderFactory)
            }
        }
        assertEquals(request.extra["a"], "aaa")
        assertEquals(request.placeholderPainter, placeholderPainterFactory)
        assertEquals(request.errorPainter, errorPainterFactory)
        assertEquals(request.skipEvent, true)
        assertNotNull(request.components)
        assertEquals(request.components.mappers.first(), mapperFactory)
        assertEquals(request.components.keyers.first(), keyerFactory)
        assertEquals(request.components.fetcherFactories.first(), fetcherFactory)
        assertEquals(request.components.decoderFactories.first(), decoderFactory)
    }
}
