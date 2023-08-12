package com.seiko.imageloader.screenshot

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RoborazziRule
import com.seiko.imageloader.ImageLoader
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
class ChangeImageUrlTest : ChangeImageUrlCommonTest() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val roborazziRule = RoborazziRule(
        composeRule = composeTestRule,
        captureRoot = composeTestRule.onRoot(),
        options = RoborazziRule.Options(
            captureType = RoborazziRule.CaptureType.AllImage(),
            outputDirectoryPath = "src/androidUnitTest/snapshots/images",
        ),
    )

    @Test
    fun test_image_change() {
        try {
            composeTestRule.setContent {
                TestUI()
            }
            (0 until 3).forEach { _ ->
                composeTestRule
                    .onNodeWithTag(BUTTON_TAG)
                    .performClick()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
