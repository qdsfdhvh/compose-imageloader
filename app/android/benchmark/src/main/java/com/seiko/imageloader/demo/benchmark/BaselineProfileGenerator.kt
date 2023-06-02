package com.seiko.imageloader.demo.benchmark

import androidx.benchmark.macro.ExperimentalStableBaselineProfilesApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.uiautomator.By
import org.junit.Rule
import org.junit.Test

class BaselineProfileGenerator {
    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @OptIn(ExperimentalStableBaselineProfilesApi::class)
    @Test
    fun startupBaselineProfile() =
        baselineProfileRule.collectStableBaselineProfile(
            packageName = "com.seiko.imageloader.demo",
            // Iteration values recommended by AndroidX folks
            maxIterations = 15,
            stableIterations = 3,
            profileBlock = {
                pressHome()
                startActivityAndWait()
                device.waitForIdle()
                device.run {
                    findObject(By.text("Network")).click()
                    waitForIdle()
                    findObject(By.desc("back")).click()
                    waitForIdle()
                    findObject(By.text("Gif")).click()
                    waitForIdle()
                    findObject(By.desc("back")).click()
                    waitForIdle()
                    findObject(By.text("Svg")).click()
                    waitForIdle()
                    findObject(By.desc("back")).click()
                    waitForIdle()
                }
            },
        )
}
