package com.seiko.imageloader.model

import androidx.compose.ui.graphics.ImageBitmapConfig
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
import kotlin.test.assertNotEquals
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
                imageBitmapConfig = ImageBitmapConfig.Alpha8
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
        assertEquals(options.imageBitmapConfig, ImageBitmapConfig.Alpha8)
        assertEquals(options.scale, Scale.FIT)
        assertEquals(options.memoryCachePolicy, CachePolicy.DISABLED)
        assertEquals(options.diskCachePolicy, CachePolicy.READ_ONLY)
        assertEquals(options.repeatCount, 5)
        assertEquals(options.maxImageSize, 100)
        assertEquals(options.extra["a"], "aa")
    }

    @Test
    fun image_request_other_params_test() {
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
            skipEvent = true
            components {
                add(mapperFactory)
                add(keyerFactory)
                add(fetcherFactory)
                add(decoderFactory)
            }
        }
        assertEquals(request.extra["a"], "aaa")
        assertEquals(request.skipEvent, true)
        assertNotNull(request.components)
        assertEquals(request.components.mappers.first(), mapperFactory)
        assertEquals(request.components.keyers.first(), keyerFactory)
        assertEquals(request.components.fetcherFactories.first(), fetcherFactory)
        assertEquals(request.components.decoderFactories.first(), decoderFactory)
    }

    @Test
    fun image_request_compare_test() {
        val request1 = ImageRequest {
            data("aa")
            skipEvent = true
        }
        assertNotEquals(request1, ImageRequest("aa"))
        assertEquals(request1, ImageRequest("aa") { skipEvent = true })
        val request2 = ImageRequest {
            data("aa")
            extra {
                put("a", "b")
            }
        }
        assertNotEquals(request2, ImageRequest("aa"))
        assertEquals(request2, ImageRequest("aa") { extra { put("a", "b") } })
    }
}
