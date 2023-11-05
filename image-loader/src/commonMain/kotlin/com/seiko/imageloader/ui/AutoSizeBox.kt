package com.seiko.imageloader.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.Constraints
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.model.ImageAction
import com.seiko.imageloader.model.ImageEvent
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.option.AsyncSizeResolver
import com.seiko.imageloader.option.SizeResolver
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun AutoSizeBox(
    request: ImageRequest,
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader = LocalImageLoader.current,
    contentAlignment: Alignment = Alignment.Center,
    propagateMinConstraints: Boolean = false,
    isOnlyPostFirstEvent: Boolean = true,
    content: @Composable BoxScope.(ImageAction) -> Unit,
) {
    var action by remember {
        mutableStateOf<ImageAction>(ImageEvent.Start)
    }
    Box(
        modifier = modifier.autoSizeBoxNode(
            request = request,
            imageLoader = imageLoader,
            onImageActionChange = { action = it },
            isOnlyPostFirstEvent = isOnlyPostFirstEvent,
        ),
        contentAlignment = contentAlignment,
        propagateMinConstraints = propagateMinConstraints,
    ) {
        content(action)
    }
}

private fun Modifier.autoSizeBoxNode(
    request: ImageRequest,
    imageLoader: ImageLoader,
    onImageActionChange: (ImageAction) -> Unit,
    isOnlyPostFirstEvent: Boolean,
): Modifier = this then AutoSizeBoxNodeElement(
    request = request,
    imageLoader = imageLoader,
    onImageActionChange = onImageActionChange,
    isOnlyPostFirstEvent = isOnlyPostFirstEvent,
)

private data class AutoSizeBoxNodeElement(
    val request: ImageRequest,
    val imageLoader: ImageLoader,
    val onImageActionChange: (ImageAction) -> Unit,
    val isOnlyPostFirstEvent: Boolean,
) : ModifierNodeElement<AutoSizeBoxNode>() {

    override fun create(): AutoSizeBoxNode {
        return AutoSizeBoxNode(
            request = request,
            imageLoader = imageLoader,
            onImageActionChange = onImageActionChange,
        )
    }

    override fun update(node: AutoSizeBoxNode) {
        node.update(
            request = request,
            imageLoader = imageLoader,
            onImageActionChange = onImageActionChange,
            isOnlyPostFirstEvent = isOnlyPostFirstEvent,
        )
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "autoSizeBox"
        properties["request"] = request
        properties["imageLoader"] = imageLoader
        properties["onImageActionChange"] = onImageActionChange
    }
}

private class AutoSizeBoxNode(
    request: ImageRequest,
    private var imageLoader: ImageLoader,
    private var onImageActionChange: (ImageAction) -> Unit,
) : Modifier.Node(), LayoutModifierNode {

    private var currentImageJob: Job? = null

    private var cachedSize: Size = Size.Unspecified

    private var isReset = false

    private var request: ImageRequest = modifyRequest(request, cachedSize)

    override val shouldAutoInvalidate: Boolean
        get() = false

    override fun onAttach() {
        super.onAttach()
        isReset = false
        launchImage()
    }

    override fun onReset() {
        super.onReset()
        isReset = true
    }

    override fun onDetach() {
        super.onDetach()
        if (!isReset) {
            cachedSize = Size.Unspecified
        }
    }

    fun update(
        request: ImageRequest,
        imageLoader: ImageLoader,
        onImageActionChange: (ImageAction) -> Unit,
        isOnlyPostFirstEvent: Boolean,
    ) {
        val finalRequest = modifyRequest(
            request = request,
            cachedSize = cachedSize,
            skipEvent = isOnlyPostFirstEvent,
        )
        val isRequestChange = this.request != finalRequest

        this.request = finalRequest
        this.imageLoader = imageLoader
        this.onImageActionChange = onImageActionChange

        if (isAttached && isRequestChange) {
            launchImage()
        }
    }

    private fun launchImage() {
        currentImageJob?.cancel()
        currentImageJob = coroutineScope.launch {
            imageLoader.async(request).collect { action ->
                onImageActionChange(action)
            }
        }
    }

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints,
    ): MeasureResult {
        cachedSize = constraints.inferredSize()

        val sizeResolver = request.sizeResolver
        if (sizeResolver is AsyncSizeResolver) {
            sizeResolver.setSize(cachedSize)
        }

        val placeable = measurable.measure(constraints)
        return layout(placeable.width, placeable.height) {
            placeable.placeRelative(0, 0)
        }
    }
}

internal fun modifyRequest(
    request: ImageRequest,
    cachedSize: Size,
    skipEvent: Boolean = false,
): ImageRequest {
    return if (request.sizeResolver == SizeResolver.Unspecified) {
        ImageRequest(request) {
            if (cachedSize.isSpecified && !cachedSize.isEmpty()) {
                size(SizeResolver(cachedSize))
            } else {
                size(AsyncSizeResolver())
            }
            this.skipEvent = skipEvent
        }
    } else {
        request
    }
}

internal data class CachedPositionAndSize(
    val position: Offset,
    val size: Size,
)
