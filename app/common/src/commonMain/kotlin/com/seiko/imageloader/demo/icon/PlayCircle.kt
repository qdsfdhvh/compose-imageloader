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
fun rememberPlayCircle(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "play_circle",
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
                moveTo(16f, 15.333f)
                verticalLineToRelative(9.334f)
                quadToRelative(0f, 0.791f, 0.667f, 1.166f)
                quadToRelative(0.666f, 0.375f, 1.333f, -0.041f)
                lineToRelative(7.333f, -4.709f)
                quadToRelative(0.584f, -0.375f, 0.584f, -1.083f)
                reflectiveQuadToRelative(-0.584f, -1.083f)
                lineTo(18f, 14.208f)
                quadToRelative(-0.667f, -0.416f, -1.333f, -0.041f)
                quadToRelative(-0.667f, 0.375f, -0.667f, 1.166f)
                close()
                moveToRelative(4f, 21.042f)
                quadToRelative(-3.375f, 0f, -6.375f, -1.292f)
                quadToRelative(-3f, -1.291f, -5.208f, -3.521f)
                quadToRelative(-2.209f, -2.229f, -3.5f, -5.208f)
                quadTo(3.625f, 23.375f, 3.625f, 20f)
                reflectiveQuadToRelative(1.292f, -6.375f)
                quadToRelative(1.291f, -3f, 3.521f, -5.208f)
                quadToRelative(2.229f, -2.209f, 5.208f, -3.5f)
                quadTo(16.625f, 3.625f, 20f, 3.625f)
                reflectiveQuadToRelative(6.375f, 1.292f)
                quadToRelative(3f, 1.291f, 5.208f, 3.521f)
                quadToRelative(2.209f, 2.229f, 3.5f, 5.208f)
                quadToRelative(1.292f, 2.979f, 1.292f, 6.354f)
                reflectiveQuadToRelative(-1.292f, 6.375f)
                quadToRelative(-1.291f, 3f, -3.521f, 5.208f)
                quadToRelative(-2.229f, 2.209f, -5.208f, 3.5f)
                quadToRelative(-2.979f, 1.292f, -6.354f, 1.292f)
                close()
                moveTo(20f, 20f)
                close()
                moveToRelative(0f, 13.75f)
                quadToRelative(5.667f, 0f, 9.708f, -4.042f)
                quadTo(33.75f, 25.667f, 33.75f, 20f)
                reflectiveQuadToRelative(-4.042f, -9.708f)
                quadTo(25.667f, 6.25f, 20f, 6.25f)
                reflectiveQuadToRelative(-9.708f, 4.042f)
                quadTo(6.25f, 14.333f, 6.25f, 20f)
                reflectiveQuadToRelative(4.042f, 9.708f)
                quadTo(14.333f, 33.75f, 20f, 33.75f)
                close()
            }
        }.build()
    }
}
