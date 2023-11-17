package com.seiko.imageloader.screenshot

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.option.SizeResolver
import com.seiko.imageloader.rememberImagePainter
import com.seiko.imageloader.ui.AutoSizeImage
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SampleTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_6,
        theme = "android:Theme.Material.NoActionBar",
    )

    private lateinit var imageLoader: ImageLoader

    @Before
    fun onBefore() {
        imageLoader = ImageLoader {
            interceptor {
                useDefaultInterceptors = false
                addInterceptor {
                    ImageResult.OfPainter(
                        ColorPainter(
                            when (it.request.data) {
                                "0" -> Color.Gray
                                "1" -> Color.Blue
                                else -> Color.Green
                            },
                        ),
                    )
                }
            }
        }
    }

    @Test
    fun launchComposable() {
        paparazzi.snapshot {
            Column {
                Image(
                    rememberImagePainter("0", imageLoader = imageLoader),
                    contentDescription = "image",
                    modifier = Modifier.size(100.dp),
                )
                Spacer(Modifier.height(4.dp))
                AutoSizeImage(
                    ImageRequest("1") {
                        size(SizeResolver(Size(100f, 100f)))
                    },
                    contentDescription = "image",
                    modifier = Modifier.size(100.dp),
                    imageLoader = imageLoader,
                )
            }
        }
    }
}
