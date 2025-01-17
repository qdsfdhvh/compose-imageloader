package com.seiko.imageloader.util

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.IntSize
import com.seiko.imageloader.option.Scale
import com.seiko.imageloader.util.DecodeUtils.calculateInSampleSize
import com.seiko.imageloader.util.DecodeUtils.computeDstSize
import kotlin.test.Test
import kotlin.test.assertEquals

class DecodeUtilsTest {
    @Test
    fun inSampleSizeWithFillIsCalculatedCorrectly() {
        assertEquals(calculateInSampleSize(100, 100, 50, 50, Scale.FILL), 2)
        assertEquals(calculateInSampleSize(100, 50, 50, 50, Scale.FILL), 1)
        assertEquals(calculateInSampleSize(99, 99, 50, 50, Scale.FILL), 1)
        assertEquals(calculateInSampleSize(200, 99, 50, 50, Scale.FILL), 1)
        assertEquals(calculateInSampleSize(200, 200, 50, 50, Scale.FILL), 4)
        assertEquals(calculateInSampleSize(1024, 1024, 80, 80, Scale.FILL), 8)
        assertEquals(calculateInSampleSize(50, 50, 100, 100, Scale.FILL), 1)
    }

    @Test
    fun inSampleSizeWithFitIsCalculatedCorrectly() {
        assertEquals(calculateInSampleSize(100, 100, 50, 50, Scale.FIT), 2)
        assertEquals(calculateInSampleSize(100, 50, 50, 50, Scale.FIT), 2)
        assertEquals(calculateInSampleSize(99, 99, 50, 50, Scale.FIT), 1)
        assertEquals(calculateInSampleSize(200, 99, 50, 50, Scale.FIT), 4)
        assertEquals(calculateInSampleSize(200, 200, 50, 50, Scale.FIT), 4)
        assertEquals(calculateInSampleSize(160, 1024, 80, 80, Scale.FIT), 8)
        assertEquals(calculateInSampleSize(50, 50, 100, 100, Scale.FIT), 1)
    }

    @Test
    fun computeDstSizeCalculatedCorrectly() {
        assertEquals(computeDstSize(100, 80, Size.Unspecified, Scale.FIT, 200), IntSize(100, 80))
        assertEquals(computeDstSize(100, 80, Size.Unspecified, Scale.FIT, 80), IntSize(80, 64))
        assertEquals(computeDstSize(100, 80, Size.Unspecified, Scale.FIT, 50), IntSize(50, 40))
        assertEquals(computeDstSize(100, 80, Size.Unspecified, Scale.FILL, 200), IntSize(100, 80))
        assertEquals(computeDstSize(100, 80, Size.Unspecified, Scale.FILL, 80), IntSize(80, 64))
        assertEquals(computeDstSize(100, 80, Size.Unspecified, Scale.FILL, 50), IntSize(50, 40))
        assertEquals(computeDstSize(100, 80, Size(50f, 50f), Scale.FIT, 100), IntSize(50, 40))
        assertEquals(computeDstSize(100, 80, Size(50f, 50f), Scale.FIT, 50), IntSize(50, 40))
        assertEquals(computeDstSize(100, 80, Size(50f, 50f), Scale.FIT, 25), IntSize(25, 20))
        assertEquals(computeDstSize(100, 80, Size(50f, 50f), Scale.FILL, 100), IntSize(63, 50))
        assertEquals(computeDstSize(100, 80, Size(50f, 50f), Scale.FILL, 50), IntSize(50, 40))
        assertEquals(computeDstSize(100, 80, Size(50f, 50f), Scale.FILL, 25), IntSize(25, 20))
        assertEquals(computeDstSize(80, 100, Size.Unspecified, Scale.FIT, 200), IntSize(80, 100))
        assertEquals(computeDstSize(80, 100, Size.Unspecified, Scale.FIT, 80), IntSize(64, 80))
        assertEquals(computeDstSize(80, 100, Size.Unspecified, Scale.FIT, 50), IntSize(40, 50))
        assertEquals(computeDstSize(80, 100, Size.Unspecified, Scale.FILL, 200), IntSize(80, 100))
        assertEquals(computeDstSize(80, 100, Size.Unspecified, Scale.FILL, 80), IntSize(64, 80))
        assertEquals(computeDstSize(80, 100, Size.Unspecified, Scale.FILL, 50), IntSize(40, 50))
        assertEquals(computeDstSize(80, 100, Size(50f, 50f), Scale.FIT, 100), IntSize(40, 50))
        assertEquals(computeDstSize(80, 100, Size(50f, 50f), Scale.FIT, 50), IntSize(40, 50))
        assertEquals(computeDstSize(80, 100, Size(50f, 50f), Scale.FIT, 25), IntSize(20, 25))
        assertEquals(computeDstSize(80, 100, Size(50f, 50f), Scale.FILL, 100), IntSize(50, 63))
        assertEquals(computeDstSize(80, 100, Size(50f, 50f), Scale.FILL, 50), IntSize(40, 50))
        assertEquals(computeDstSize(80, 100, Size(50f, 50f), Scale.FILL, 25), IntSize(20, 25))

        assertEquals(computeDstSize(80, 100, Size.Zero, Scale.FIT, 200), IntSize(80, 100))
        assertEquals(computeDstSize(80, 100, Size(Float.NaN, 0f), Scale.FIT, 200), IntSize(80, 100))
        assertEquals(computeDstSize(80, 100, Size(0f, Float.NaN), Scale.FIT, 200), IntSize(80, 100))
    }
}
