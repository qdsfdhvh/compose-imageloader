package com.seiko.imageloader.screenshot

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.intercept.addInterceptor
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.rememberImagePainter

abstract class ChangeImageUrlCommonTest {

    @Composable
    protected fun TestUI() {
        val imageLoader = remember {
            ImageLoader {
                interceptor {
                    useDefaultInterceptors = false
                    addInterceptor {
                        val color = when (it.request.data as Int) {
                            0 -> Color.White
                            1 -> Color.Blue
                            2 -> Color.Green
                            3 -> Color.Gray
                            else -> Color.Red // no display
                        }
                        ImageResult.OfPainter(ColorPainter(color))
                    }
                }
            }
        }
        var index by remember { mutableStateOf(0) }
        Image(
            rememberImagePainter(remember(index) { ImageRequest(index) }, imageLoader),
            contentDescription = "change url",
            modifier = Modifier.size(80.dp)
                .testTag(BUTTON_TAG)
                .clickable { index++ },
        )
    }

    companion object {
        const val BUTTON_TAG = "MyComposeButton"
    }
}
