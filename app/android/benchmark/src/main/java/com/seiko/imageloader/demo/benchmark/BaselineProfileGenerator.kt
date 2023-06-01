package com.seiko.imageloader.demo.benchmark

import androidx.benchmark.macro.ExperimentalStableBaselineProfilesApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import org.junit.Rule
import org.junit.Test

class BaselineProfileGenerator {
    @get:Rule val baselineProfileRule = BaselineProfileRule()

    @OptIn(ExperimentalStableBaselineProfilesApi::class)
    @Test
    fun startupBaselineProfile() =
        baselineProfileRule.collectStableBaselineProfile(
            packageName = "com.seiko.imageloader.demo",
            // Iteration values recommended by AndroidX folks
            maxIterations = 15,
            stableIterations = 3,
            profileBlock = { startActivityAndWait() },
        )
}
