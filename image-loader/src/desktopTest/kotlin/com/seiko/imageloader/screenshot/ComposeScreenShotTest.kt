package com.seiko.imageloader.screenshot

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runDesktopComposeUiTest
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.InternalRoborazziApi
import com.github.takahirom.roborazzi.RoborazziContext
import io.github.takahirom.roborazzi.captureRoboImage
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
class ComposeScreenShotTest : ComposeScreenShotCommonTest() {

    @OptIn(ExperimentalRoborazziApi::class, InternalRoborazziApi::class)
    @Before
    fun initRoborazziConfig() {
        RoborazziContext.setRuleOverrideOutputDirectory(
            outputDirectory = "src/desktopTest/snapshots/images",
        )
    }

    @Test
    fun test_load_image() = runMyDesktopComposeUiTest(width = 100 + 8 + 100) {
        TestLoadImageUI()
    }

    @Test
    fun test_placeholder_painter() = runMyDesktopComposeUiTest {
        TestPlaceholderPainterUI()
    }

    @Test
    fun test_error_painter() = runMyDesktopComposeUiTest {
        TestErrorPainterUI()
    }

    protected fun runMyDesktopComposeUiTest(
        width: Int = 100,
        height: Int = 100,
        content: @Composable () -> Unit,
    ) =
        runDesktopComposeUiTest(width = width, height = height) {
            setContent(content)
            onRoot().captureRoboImage()
        }
}
