package com.seiko.imageloader.demo.icon

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Composable
fun rememberVisibility(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "visibility",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f,
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero,
            ) {
                moveTo(20f, 26.208f)
                quadToRelative(2.958f, 0f, 5f, -2.041f)
                quadToRelative(2.042f, -2.042f, 2.042f, -5f)
                quadToRelative(0f, -2.959f, -2.042f, -5f)
                quadToRelative(-2.042f, -2.042f, -5f, -2.042f)
                reflectiveQuadToRelative(-5f, 2.042f)
                quadToRelative(-2.042f, 2.041f, -2.042f, 5f)
                quadToRelative(0f, 2.958f, 2.042f, 5f)
                quadToRelative(2.042f, 2.041f, 5f, 2.041f)
                close()
                moveToRelative(0f, -2.5f)
                quadToRelative(-1.917f, 0f, -3.229f, -1.312f)
                quadToRelative(-1.313f, -1.313f, -1.313f, -3.229f)
                quadToRelative(0f, -1.917f, 1.313f, -3.229f)
                quadToRelative(1.312f, -1.313f, 3.229f, -1.313f)
                reflectiveQuadToRelative(3.229f, 1.313f)
                quadToRelative(1.313f, 1.312f, 1.313f, 3.229f)
                quadToRelative(0f, 1.916f, -1.313f, 3.229f)
                quadToRelative(-1.312f, 1.312f, -3.229f, 1.312f)
                close()
                moveToRelative(0f, 7.75f)
                quadToRelative(-5.667f, 0f, -10.312f, -3.041f)
                quadToRelative(-4.646f, -3.042f, -7.146f, -8.042f)
                quadToRelative(-0.125f, -0.25f, -0.188f, -0.563f)
                quadToRelative(-0.062f, -0.312f, -0.062f, -0.645f)
                quadToRelative(0f, -0.334f, 0.062f, -0.646f)
                quadToRelative(0.063f, -0.313f, 0.188f, -0.563f)
                quadToRelative(2.5f, -5f, 7.146f, -8.041f)
                quadTo(14.333f, 6.875f, 20f, 6.875f)
                reflectiveQuadToRelative(10.312f, 3.042f)
                quadToRelative(4.646f, 3.041f, 7.146f, 8.041f)
                quadToRelative(0.125f, 0.25f, 0.209f, 0.563f)
                quadToRelative(0.083f, 0.312f, 0.083f, 0.646f)
                quadToRelative(0f, 0.333f, -0.083f, 0.645f)
                quadToRelative(-0.084f, 0.313f, -0.209f, 0.563f)
                quadToRelative(-2.5f, 5f, -7.146f, 8.042f)
                quadTo(25.667f, 31.458f, 20f, 31.458f)
                close()
                moveToRelative(0f, -12.291f)
                close()
                moveToRelative(0f, 9.625f)
                quadToRelative(4.875f, 0f, 8.979f, -2.604f)
                quadToRelative(4.104f, -2.605f, 6.229f, -7.021f)
                quadTo(33.083f, 14.75f, 29f, 12.146f)
                reflectiveQuadToRelative(-9f, -2.604f)
                quadToRelative(-4.875f, 0f, -8.979f, 2.604f)
                quadToRelative(-4.104f, 2.604f, -6.271f, 7.021f)
                quadToRelative(2.167f, 4.416f, 6.25f, 7.021f)
                quadToRelative(4.083f, 2.604f, 9f, 2.604f)
                close()
            }
        }.build()
    }
}
