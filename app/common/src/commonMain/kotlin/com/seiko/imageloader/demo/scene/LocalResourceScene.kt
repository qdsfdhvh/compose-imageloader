package com.seiko.imageloader.demo.scene

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.demo.MR
import com.seiko.imageloader.model.ninePatch

@Composable
fun LocalResourceScene(
    onBack: () -> Unit,
) {
    BackScene(
        onBack = onBack,
        title = { Text("Local Resource") },
        backgroundColor = Color.Gray,
    ) { innerPadding ->
        LazyColumn(
            Modifier.padding(innerPadding).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        ) {
            item {
                Row {
                    ImageItem(
                        MR.files.chat_from_bg_normal_9,
                        modifier = Modifier.weight(1f).height(100.dp),
                        contentScale = ContentScale.Fit,
                        block = remember {
                            {
                                // image size: 74 x 62 px, center rect:[42, 40, 42 + 2, 40 + 2]
                                ninePatch {
                                    top = 40
                                    bottom = 40 + 2
                                    left = 42
                                    right = 42 + 2
                                    skipPadding = 1
                                }
                            }
                        },
                    )
                    Spacer(Modifier.width(8.dp))
                    ImageItem(
                        MR.files.chat_from_bg_normal_9,
                        modifier = Modifier.weight(1f).height(100.dp),
                        contentScale = ContentScale.Fit,
                    )
                }
            }
            item {
                ImageItem(MR.files.cat)
            }
            item {
                ImageItem(MR.files.collection_logo)
            }
            item {
                ImageItem(MR.images.car_black)
            }
            item {
                ImageItem(MR.colors.valueColor)
            }
            item {
                ImageItem(MR.colors.valueColor2)
            }
        }
    }
}
