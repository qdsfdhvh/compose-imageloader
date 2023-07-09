package com.seiko.imageloader.screenshot

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RoborazziRule
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.intercept.Interceptor
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.rememberImagePainter
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [32])
class ComposeScreenShotTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val roborazziRule = RoborazziRule(
        composeRule = composeTestRule,
        captureRoot = composeTestRule.onRoot(),
        options = RoborazziRule.Options(
            outputDirectoryPath = "src/androidUnitTest/snapshots/images",
        ),
    )

    @Test
    fun loadImage() {
        val url = "https://example.com/url.jpg"
        val imageLoader = ImageLoader {
            interceptor {
                useDefaultInterceptors = false
                addInterceptor(
                    Interceptor { chain ->
                        val color = if (url == chain.request.data) Color.Green else Color.Red
                        ImageResult.Painter(ColorPainter(color))
                    },
                )
            }
        }
        composeTestRule.setContent {
            Row {
                Image(
                    rememberImagePainter(url, imageLoader),
                    contentDescription = "green",
                    modifier = Modifier.size(100.dp),
                )
                Spacer(Modifier.width(8.dp))
                Image(
                    rememberImagePainter("aa", imageLoader),
                    contentDescription = "red",
                    modifier = Modifier.size(100.dp),
                )
            }
        }
    }
}
