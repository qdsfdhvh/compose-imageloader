package com.seiko.imageloader.screenshot

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runDesktopComposeUiTest
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.InternalRoborazziApi
import com.github.takahirom.roborazzi.RoborazziContext
import com.github.takahirom.roborazzi.RoborazziOptions
import io.github.takahirom.roborazzi.captureRoboImage
import org.junit.Before
import org.junit.Test

class ChangeImageUrlTest : ChangeImageUrlCommonTest() {

    @OptIn(ExperimentalRoborazziApi::class, InternalRoborazziApi::class)
    @Before
    fun initRoborazziConfig() {
        RoborazziContext.setRuleOverrideOutputDirectory(
            outputDirectory = "build/outputs/roborazzi/desktop",
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun test_image_change() = runDesktopComposeUiTest(width = 80, height = 80) {
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
