package com.seiko.imageloader.compose

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

internal data class CachedPositionAndSize(
    val position: Offset,
    val size: Size,
)
