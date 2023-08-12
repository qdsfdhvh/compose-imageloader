package com.seiko.imageloader.screenshot

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runComposeUiTest
import com.github.takahirom.roborazzi.RoborazziOptions
import io.github.takahirom.roborazzi.captureRoboImage
import org.junit.Test

class ChangeImageUrlTest : ChangeImageUrlCommonTest() {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun test_image_change() = runComposeUiTest {
        setContent {
            TestUI()
        }
        val roborazziOptions = RoborazziOptions(
            captureType = RoborazziOptions.CaptureType.Screenshot(),
            compareOptions = RoborazziOptions.CompareOptions(changeThreshold = 0F),
        )
        onRoot().captureRoboImage(
            // filePath = "src/desktopTest/snapshots/images/1.png",
            roborazziOptions = roborazziOptions,
        )
    }
}
