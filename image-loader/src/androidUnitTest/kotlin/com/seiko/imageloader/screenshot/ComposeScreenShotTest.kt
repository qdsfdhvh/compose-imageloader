package com.seiko.imageloader.screenshot

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RoborazziRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [32])
class ComposeScreenShotTest : ComposeScreenShotCommonTest() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val roborazziRule = RoborazziRule(
        composeRule = composeTestRule,
        captureRoot = composeTestRule.onRoot(),
        options = RoborazziRule.Options(
            captureType = RoborazziRule.CaptureType.LastImage(),
            outputDirectoryPath = "src/androidUnitTest/snapshots/images",
        ),
    )

    @Test
    fun test_load_image() = composeTestRule.setContent {
        TestLoadImageUI()
    }

    @Test
    fun test_placeholder_painter() = composeTestRule.setContent {
        TestPlaceholderPainterUI()
    }

    @Test
    fun test_error_painter() = composeTestRule.setContent {
        TestErrorPainterUI()
    }
}
