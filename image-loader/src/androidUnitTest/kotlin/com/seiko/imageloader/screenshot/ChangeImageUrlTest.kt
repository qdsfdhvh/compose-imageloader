package com.seiko.imageloader.screenshot

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.InternalRoborazziApi
import com.github.takahirom.roborazzi.RoborazziContext
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Before
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

    @OptIn(ExperimentalRoborazziApi::class, InternalRoborazziApi::class)
    @Before
    fun initRoborazziConfig() {
        RoborazziContext.setRuleOverrideOutputDirectory(
            outputDirectory = "src/androidUnitTest/snapshots/images",
        )
    }

    @Test
    fun test_image_change() = with(composeTestRule) {
        setContent {
            TestUI()
        }
        val roborazziOptions = RoborazziOptions(
            captureType = RoborazziOptions.CaptureType.Screenshot(),
            compareOptions = RoborazziOptions.CompareOptions(changeThreshold = 0F),
        )
        onRoot().captureRoboImage(
            roborazziOptions = roborazziOptions,
        )
        (0..2).forEach { _ ->
            onNodeWithTag(BUTTON_TAG).performClick()
            onRoot().captureRoboImage(
                roborazziOptions = roborazziOptions,
            )
        }
    }
}
