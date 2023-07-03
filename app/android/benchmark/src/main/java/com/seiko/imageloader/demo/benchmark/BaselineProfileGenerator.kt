package com.seiko.imageloader.demo.benchmark

import androidx.benchmark.macro.junit4.BaselineProfileRule
import org.junit.Rule
import org.junit.Test

class BaselineProfileGenerator {
    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun startupBaselineProfile() =
        baselineProfileRule.collect(
            packageName = "com.seiko.imageloader.demo",
            // Iteration values recommended by AndroidX folks
            maxIterations = 15,
            stableIterations = 3,
            profileBlock = {
                pressHome()
                startActivityAndWait()
                device.waitForIdle()
            },
        )
}
