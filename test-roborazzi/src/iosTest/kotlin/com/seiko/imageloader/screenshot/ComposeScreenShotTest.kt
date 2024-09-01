package com.seiko.imageloader.screenshot

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runSkikoComposeUiTest
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ComposeScreenShotTest : ComposeScreenShotCommonTest() {

    // @OptIn(ExperimentalRoborazziApi::class, InternalRoborazziApi::class)
    // @Before
    // fun initRoborazziConfig() {
    //     RoborazziContext.setRuleOverrideOutputDirectory(
    //         outputDirectory = "build/outputs/roborazzi/desktop",
    //     )
    // }

    @Test
    fun desktop_test_load_image() = runMyDesktopComposeUiTest(width = 100 + 8 + 100) {
        TestLoadImageUI()
    }

    @Test
    fun desktop_test_placeholder_painter() = runMyDesktopComposeUiTest {
        TestPlaceholderPainterUI()
    }

    @Test
    fun desktop_test_error_painter() = runMyDesktopComposeUiTest {
        TestErrorPainterUI()
    }

    private fun runMyDesktopComposeUiTest(
        width: Int = 100,
        height: Int = 100,
        content: @Composable () -> Unit,
    ) = runSkikoComposeUiTest(size = Size(width.toFloat(), height.toFloat())) {
        setContent(content)
        onRoot().captureRoboImage(
            this,
            filePath = "",
        )
    }
}
