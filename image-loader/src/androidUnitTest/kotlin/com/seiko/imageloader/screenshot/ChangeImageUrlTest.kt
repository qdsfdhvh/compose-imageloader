package com.seiko.imageloader.screenshot

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
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
class ChangeImageUrlTest : ChangeImageUrlCommonTest() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val roborazziRule = RoborazziRule(
        composeRule = composeTestRule,
        captureRoot = composeTestRule.onRoot(),
        options = RoborazziRule.Options(
            captureType = RoborazziRule.CaptureType.AllImage(),
            outputDirectoryPath = "build/outputs/roborazzi/android",
        ),
    )

    @Test
    fun test_image_change() = with(composeTestRule) {
        setContent {
            TestUI()
        }
        (0..2).forEach { _ ->
            onNodeWithTag(BUTTON_TAG).performClick()
        }
    }
}
