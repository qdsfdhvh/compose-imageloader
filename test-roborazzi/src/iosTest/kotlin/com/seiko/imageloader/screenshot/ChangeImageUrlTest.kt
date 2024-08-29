package com.seiko.imageloader.screenshot

import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.Test

class ChangeImageUrlTest : ChangeImageUrlCommonTest() {

    @Test
    fun ios_test_image_change() = runComposeUiTest {
        setContent {
            TestUI()
        }
        // val roborazziOptions = RoborazziOptions(
        //     captureType = RoborazziOptions.CaptureType.Screenshot(),
        //     compareOptions = RoborazziOptions.CompareOptions(changeThreshold = 0F),
        // )
        onRoot().captureRoboImage(
            this,
            filePath = "ios_test_image_change.png",
            // roborazziOptions = roborazziOptions,
        )
        (0..2).forEach {
            onNodeWithTag(BUTTON_TAG).performClick()
            onRoot().captureRoboImage(
                this,
                filePath = "ios_test_image_change_$it.png",
            )
        }
    }
}
