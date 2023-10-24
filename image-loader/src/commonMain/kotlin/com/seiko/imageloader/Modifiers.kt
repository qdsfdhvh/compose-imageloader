package com.seiko.imageloader

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.unit.Constraints
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.option.AsyncSizeResolver

fun Modifier.imageNode(
    requestState: State<ImageRequest>,
): Modifier = this.then(ImageNodeElement(requestState))

private class ImageNodeElement(
    private val requestState: State<ImageRequest>,
) : ModifierNodeElement<ImageNode>() {
    override fun create(): ImageNode {
        return ImageNode(requestState)
    }

    override fun equals(other: Any?): Boolean {
        val otherModifier = other as? ImageNodeElement ?: return false
        return requestState == otherModifier.requestState
    }

    override fun hashCode(): Int {
        return requestState.hashCode()
    }

    override fun update(node: ImageNode) {
        node.requestState = requestState
    }
}

private class ImageNode(
    var requestState: State<ImageRequest>,
) : Modifier.Node(), LayoutModifierNode {

    val request: ImageRequest by requestState

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints,
    ): MeasureResult {
        val sizeResolver = request.sizeResolver
        if (sizeResolver is AsyncSizeResolver) {
            val inferredSize = constraints.inferredSize()
            sizeResolver.setSize(inferredSize)
        }

        val placeable = measurable.measure(constraints)
        return layout(placeable.width, placeable.height) {
            placeable.placeRelative(0, 0)
        }
    }
}

private fun Constraints.hasFixedSize() = hasFixedWidth && hasFixedHeight

internal fun Constraints.inferredSize(): Size {
    if (!hasBoundedWidth || !hasBoundedHeight) return Size.Unspecified
    return Size(maxWidth.toFloat(), maxHeight.toFloat())
}
