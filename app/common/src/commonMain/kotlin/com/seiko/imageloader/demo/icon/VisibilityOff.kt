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
fun rememberVisibilityOff(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "visibility_off",
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
                moveTo(26.25f, 22.417f)
                lineTo(24.333f, 20.5f)
                quadToRelative(0.875f, -2.75f, -1.145f, -4.604f)
                quadToRelative(-2.021f, -1.854f, -4.521f, -1.063f)
                lineToRelative(-1.917f, -1.916f)
                quadToRelative(0.708f, -0.417f, 1.542f, -0.605f)
                quadToRelative(0.833f, -0.187f, 1.708f, -0.187f)
                quadToRelative(2.958f, 0f, 5f, 2.042f)
                quadToRelative(2.042f, 2.041f, 2.042f, 5f)
                quadToRelative(0f, 0.875f, -0.209f, 1.729f)
                quadToRelative(-0.208f, 0.854f, -0.583f, 1.521f)
                close()
                moveToRelative(5.25f, 5.25f)
                lineToRelative(-1.792f, -1.792f)
                quadToRelative(1.875f, -1.375f, 3.313f, -3.104f)
                quadToRelative(1.437f, -1.729f, 2.187f, -3.604f)
                quadToRelative(-2.041f, -4.459f, -6.083f, -7.042f)
                reflectiveQuadToRelative(-8.833f, -2.583f)
                quadToRelative(-1.542f, 0f, -3.209f, 0.271f)
                quadToRelative(-1.666f, 0.27f, -2.708f, 0.729f)
                lineToRelative(-2.042f, -2.084f)
                quadToRelative(1.5f, -0.666f, 3.625f, -1.125f)
                quadToRelative(2.125f, -0.458f, 4.167f, -0.458f)
                quadToRelative(5.625f, 0f, 10.271f, 3.021f)
                quadToRelative(4.646f, 3.021f, 7.104f, 8.062f)
                quadToRelative(0.125f, 0.25f, 0.188f, 0.563f)
                quadToRelative(0.062f, 0.312f, 0.062f, 0.646f)
                quadToRelative(0f, 0.333f, -0.062f, 0.666f)
                quadToRelative(-0.063f, 0.334f, -0.188f, 0.542f)
                quadToRelative(-1.042f, 2.208f, -2.562f, 4.021f)
                quadToRelative(-1.521f, 1.812f, -3.438f, 3.271f)
                close()
                moveToRelative(1.083f, 8.458f)
                lineToRelative(-5.916f, -5.833f)
                quadToRelative(-1.417f, 0.541f, -3.146f, 0.854f)
                quadToRelative(-1.729f, 0.312f, -3.521f, 0.312f)
                quadToRelative(-5.708f, 0f, -10.375f, -3.02f)
                quadToRelative(-4.667f, -3.021f, -7.125f, -8.063f)
                quadToRelative(-0.125f, -0.292f, -0.167f, -0.583f)
                quadToRelative(-0.041f, -0.292f, -0.041f, -0.625f)
                quadToRelative(0f, -0.334f, 0.062f, -0.667f)
                quadToRelative(0.063f, -0.333f, 0.146f, -0.583f)
                quadToRelative(0.917f, -1.792f, 2.188f, -3.479f)
                quadToRelative(1.27f, -1.688f, 2.937f, -3.146f)
                lineTo(3.583f, 7.167f)
                quadToRelative(-0.416f, -0.375f, -0.416f, -0.917f)
                reflectiveQuadToRelative(0.416f, -0.917f)
                quadToRelative(0.375f, -0.375f, 0.917f, -0.375f)
                reflectiveQuadToRelative(0.958f, 0.375f)
                lineToRelative(29f, 29f)
                quadToRelative(0.334f, 0.375f, 0.334f, 0.875f)
                reflectiveQuadToRelative(-0.375f, 0.917f)
                quadToRelative(-0.375f, 0.417f, -0.917f, 0.417f)
                reflectiveQuadToRelative(-0.917f, -0.417f)
                close()
                moveToRelative(-23.125f, -23f)
                quadToRelative(-1.416f, 1.083f, -2.729f, 2.771f)
                quadToRelative(-1.312f, 1.687f, -1.979f, 3.271f)
                quadToRelative(2.083f, 4.458f, 6.188f, 7.041f)
                quadToRelative(4.104f, 2.584f, 9.27f, 2.584f)
                quadToRelative(1.25f, 0f, 2.459f, -0.146f)
                quadToRelative(1.208f, -0.146f, 1.875f, -0.438f)
                lineToRelative(-2.334f, -2.333f)
                quadToRelative(-0.458f, 0.167f, -1.062f, 0.25f)
                quadToRelative(-0.604f, 0.083f, -1.146f, 0.083f)
                quadToRelative(-2.917f, 0f, -4.979f, -2.041f)
                quadToRelative(-2.063f, -2.042f, -2.063f, -5f)
                quadToRelative(0f, -0.584f, 0.084f, -1.146f)
                quadToRelative(0.083f, -0.563f, 0.25f, -1.063f)
                close()
                moveToRelative(12.625f, 5.333f)
                close()
                moveTo(17.042f, 21f)
                close()
            }
        }.build()
    }
}
