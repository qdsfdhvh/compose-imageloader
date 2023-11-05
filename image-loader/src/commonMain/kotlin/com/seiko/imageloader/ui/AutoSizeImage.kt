package com.seiko.imageloader.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.withSave
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.times
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.node.invalidateMeasurement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.toOffset
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.model.ImageEvent
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.option.AsyncSizeResolver
import com.seiko.imageloader.toPainter
import com.seiko.imageloader.util.AnimationPainter
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

// AutoSizeImage ≈ AutoSizeBox + Painter
@Composable
fun AutoSizeImage(
    request: ImageRequest,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    imageLoader: ImageLoader = LocalImageLoader.current,
    placeholderPainter: (@Composable () -> Painter)? = null,
    errorPainter: (@Composable () -> Painter)? = null,
    isOnlyPostFirstEvent: Boolean = true,
) {
    val semantics = if (contentDescription != null) {
        Modifier.semantics {
            this.contentDescription = contentDescription
            this.role = Role.Image
        }
    } else {
        Modifier
    }
    Layout(
        modifier.then(semantics).clipToBounds().autoSizeImageNode(
            request = request,
            alignment = alignment,
            contentScale = contentScale,
            alpha = alpha,
            colorFilter = colorFilter,
            imageLoader = imageLoader,
            placeholderPainter = placeholderPainter?.invoke(),
            errorPainter = errorPainter?.invoke(),
            isOnlyPostFirstEvent = isOnlyPostFirstEvent,
        ),
    ) { _, constraints ->
        layout(constraints.minWidth, constraints.minHeight) {}
    }
}

private fun Modifier.autoSizeImageNode(
    request: ImageRequest,
    alignment: Alignment,
    contentScale: ContentScale,
    alpha: Float,
    colorFilter: ColorFilter?,
    imageLoader: ImageLoader,
    placeholderPainter: Painter?,
    errorPainter: Painter?,
    isOnlyPostFirstEvent: Boolean,
): Modifier = this then AutoSizeImageNodeElement(
    request = request,
    alignment = alignment,
    contentScale = contentScale,
    alpha = alpha,
    colorFilter = colorFilter,
    imageLoader = imageLoader,
    placeholderPainter = placeholderPainter,
    errorPainter = errorPainter,
    isOnlyPostFirstEvent = isOnlyPostFirstEvent,
)

private data class AutoSizeImageNodeElement(
    private val request: ImageRequest,
    private val alignment: Alignment,
    private val contentScale: ContentScale,
    private val alpha: Float,
    private val colorFilter: ColorFilter?,
    private val imageLoader: ImageLoader,
    private val placeholderPainter: Painter?,
    private val errorPainter: Painter?,
    private val isOnlyPostFirstEvent: Boolean,
) : ModifierNodeElement<AutoSizeImageNode>() {

    override fun create(): AutoSizeImageNode {
        return AutoSizeImageNode(
            request = request,
            alignment = alignment,
            contentScale = contentScale,
            alpha = alpha,
            colorFilter = colorFilter,
            imageLoader = imageLoader,
            placeholderPainter = placeholderPainter,
            errorPainter = errorPainter,
        )
    }

    override fun update(node: AutoSizeImageNode) {
        node.update(
            request = request,
            alignment = alignment,
            contentScale = contentScale,
            alpha = alpha,
            colorFilter = colorFilter,
            imageLoader = imageLoader,
            placeholderPainter = placeholderPainter,
            errorPainter = errorPainter,
            isOnlyPostFirstEvent = isOnlyPostFirstEvent,
        )
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "autoSizeImage"
        properties["request"] = request
        properties["alignment"] = alignment
        properties["contentScale"] = contentScale
        properties["alpha"] = alpha
        properties["colorFilter"] = colorFilter
        properties["imageLoader"] = imageLoader
        properties["placeholderPainter"] = placeholderPainter
        properties["errorPainter"] = errorPainter
    }
}

private class AutoSizeImageNode(
    request: ImageRequest,
    private var alignment: Alignment,
    private var contentScale: ContentScale,
    private var alpha: Float,
    private var colorFilter: ColorFilter?,
    private var imageLoader: ImageLoader,
    private var placeholderPainter: Painter?,
    private var errorPainter: Painter?,
) : Modifier.Node(), LayoutModifierNode, DrawModifierNode, CompositionLocalConsumerModifierNode {

    private var currentImageJob: Job? = null
    private var currentPlayerJob: Job? = null

    private var cachedSize: Size = Size.Unspecified

    private var drawPainter: Painter? = null
    private var drawPainterPositionAndSize: CachedPositionAndSize? = null

    private var hasFixedSize: Boolean = false
    private var isReset: Boolean = false

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
        // if this node is reset from pool, not need to reset size
        if (!isReset) {
            hasFixedSize = false
            cachedSize = Size.Unspecified
        }
        updatePainter(null)
    }

    fun update(
        request: ImageRequest,
        alignment: Alignment,
        contentScale: ContentScale,
        alpha: Float,
        colorFilter: ColorFilter?,
        imageLoader: ImageLoader,
        placeholderPainter: Painter?,
        errorPainter: Painter?,
        isOnlyPostFirstEvent: Boolean,
    ) {
        val finalRequest = modifyRequest(
            request = request,
            cachedSize = cachedSize,
            skipEvent = isOnlyPostFirstEvent,
        )
        val isRequestChange = this.request != finalRequest

        this.request = finalRequest
        this.alignment = alignment
        this.contentScale = contentScale
        this.alpha = alpha
        this.colorFilter = colorFilter
        this.imageLoader = imageLoader
        this.placeholderPainter = placeholderPainter
        this.errorPainter = errorPainter

        if (isAttached && isRequestChange) {
            launchImage()
        }
    }

    private fun launchImage() {
        currentImageJob?.cancel()
        currentImageJob = coroutineScope.launch {
            imageLoader.async(request).collect { action ->
                when (action) {
                    is ImageEvent -> placeholderPainter
                    is ImageResult.OfPainter -> action.painter
                    is ImageResult.OfBitmap -> action.bitmap.toPainter()
                    is ImageResult.OfImage -> action.image.toPainter()
                    is ImageResult.OfError -> errorPainter
                    is ImageResult.OfSource -> errorPainter
                }?.let { painter ->
                    updatePainter(painter)
                }
            }
        }
    }

    private fun updatePainter(painter: Painter?) {
        (drawPainter as? RememberObserver)?.onAbandoned()
        currentPlayerJob?.cancel()
        currentPlayerJob = null

        drawPainter = painter

        if (isAttached) {
            (painter as? RememberObserver)?.onRemembered()
            checkPainterPlay()
        }

        drawPainterPositionAndSize = null

        if (hasFixedSize) {
            invalidateDraw()
        } else {
            invalidateMeasurement()
        }
    }

    private fun checkPainterPlay() {
        val painter = drawPainter ?: return
        if (painter is AnimationPainter && painter.isPlay()) {
            currentPlayerJob?.cancel()
            currentPlayerJob = coroutineScope.launch {
                while (painter.nextPlay()) {
                    withFrameMillis { frameTimeMillis ->
                        painter.update(frameTimeMillis)
                    }
                }
            }
        }
    }

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints,
    ): MeasureResult {
        drawPainterPositionAndSize = null
        hasFixedSize = constraints.hasFixedSize()
        cachedSize = constraints.inferredSize()

        val sizeResolver = request.sizeResolver
        if (sizeResolver is AsyncSizeResolver) {
            sizeResolver.setSize(cachedSize)
        }

        val placeable = measurable.measure(
            modifyConstraints(constraints, drawPainter, contentScale),
        )
        return layout(placeable.width, placeable.height) {
            placeable.placeRelative(0, 0)
        }
    }

    override fun ContentDrawScope.draw() {
        drawPainter?.let { painter ->
            drawContext.canvas.withSave {
                val positionAndSize = drawPainterPositionAndSize
                    ?: calcPositionAndSize(painter).also {
                        drawPainterPositionAndSize = it
                    }
                val dx = positionAndSize.position.x
                val dy = positionAndSize.position.y
                val scaledSize = positionAndSize.size
                translate(dx, dy) {
                    with(painter) {
                        draw(scaledSize, alpha, colorFilter)
                    }
                }
            }
        }

        // Maintain the same pattern as Modifier.drawBehind to allow chaining of DrawModifiers
        drawContent()
    }

    private fun ContentDrawScope.calcPositionAndSize(painter: Painter): CachedPositionAndSize {
        val intrinsicSize = painter.intrinsicSize
        val srcWidth = if (intrinsicSize.hasSpecifiedAndFiniteWidth()) {
            intrinsicSize.width
        } else {
            size.width
        }

        val srcHeight = if (intrinsicSize.hasSpecifiedAndFiniteHeight()) {
            intrinsicSize.height
        } else {
            size.height
        }
        val srcSize = Size(srcWidth, srcHeight)

        // Compute the offset to translate the content based on the given alignment
        // and size to draw based on the ContentScale parameter
        val scaledSize = if (size.width != 0f && size.height != 0f) {
            srcSize * contentScale.computeScaleFactor(srcSize, size)
        } else {
            Size.Zero
        }

        val alignedPosition = alignment.align(
            IntSize(scaledSize.width.roundToInt(), scaledSize.height.roundToInt()),
            IntSize(size.width.roundToInt(), size.height.roundToInt()),
            layoutDirection,
        ).toOffset()
        return CachedPositionAndSize(
            alignedPosition,
            scaledSize,
        )
    }

    override fun equals(other: Any?): Boolean {
        return (other is AutoSizeImageNode) &&
            request == other.request &&
            alignment == other.alignment &&
            contentScale == other.contentScale &&
            alpha == other.alpha &&
            colorFilter == other.colorFilter &&
            imageLoader == other.imageLoader
    }

    override fun hashCode(): Int {
        var result = request.hashCode()
        result = 31 * result + alignment.hashCode()
        result = 31 * result + contentScale.hashCode()
        result = 31 * result + alpha.hashCode()
        result = 31 * result + colorFilter.hashCode()
        result = 31 * result + imageLoader.hashCode()
        result = 31 * result + placeholderPainter.hashCode()
        result = 31 * result + errorPainter.hashCode()
        return result
    }

    override fun toString(): String {
        return "AutoSizeImageModifier(" +
            "painter=$drawPainter, " +
            "alignment=$alignment, " +
            "contentScale=$contentScale, " +
            "alpha=$alpha, " +
            "colorFilter=$colorFilter, " +
            "imageLoader=$imageLoader, " +
            "placeholderPainter=$placeholderPainter, " +
            "errorPainter=$errorPainter)"
    }
}

private fun modifyConstraints(
    constraints: Constraints,
    painter: Painter?,
    contentScale: ContentScale,
): Constraints {
    if (constraints.hasFixedSize()) {
        return constraints.copy(
            minWidth = constraints.maxWidth,
            minHeight = constraints.maxHeight,
        )
    }

    val intrinsicSize = painter?.intrinsicSize ?: return constraints
    val intrinsicWidth = if (intrinsicSize.hasSpecifiedAndFiniteWidth()) {
        intrinsicSize.width.roundToInt()
    } else {
        constraints.minWidth
    }

    val intrinsicHeight = if (intrinsicSize.hasSpecifiedAndFiniteHeight()) {
        intrinsicSize.height.roundToInt()
    } else {
        constraints.minHeight
    }

    // Scale the width and height appropriately based on the given constraints
    // and ContentScale
    val constrainedWidth = constraints.constrainWidth(intrinsicWidth)
    val constrainedHeight = constraints.constrainHeight(intrinsicHeight)
    val scaledSize = calculateScaledSize(
        Size(constrainedWidth.toFloat(), constrainedHeight.toFloat()),
        painter,
        contentScale,
    )

    // For both width and height constraints, consume the minimum of the scaled width
    // and the maximum constraint as some scale types can scale larger than the maximum
    // available size (ex ContentScale.Crop)
    // In this case the larger of the 2 dimensions is used and the aspect ratio is
    // maintained. Even if the size of the composable is smaller, the painter will
    // draw its content clipped
    val minWidth = constraints.constrainWidth(scaledSize.width.roundToInt())
    val minHeight = constraints.constrainHeight(scaledSize.height.roundToInt())
    return constraints.copy(minWidth = minWidth, minHeight = minHeight)
}

private fun calculateScaledSize(dstSize: Size, painter: Painter, contentScale: ContentScale): Size {
    val srcWidth = if (!painter.intrinsicSize.hasSpecifiedAndFiniteWidth()) {
        dstSize.width
    } else {
        painter.intrinsicSize.width
    }

    val srcHeight = if (!painter.intrinsicSize.hasSpecifiedAndFiniteHeight()) {
        dstSize.height
    } else {
        painter.intrinsicSize.height
    }

    val srcSize = Size(srcWidth, srcHeight)
    return if (dstSize.width != 0f && dstSize.height != 0f) {
        srcSize * contentScale.computeScaleFactor(srcSize, dstSize)
    } else {
        Size.Zero
    }
}

private fun Constraints.hasFixedSize() = hasFixedWidth && hasFixedHeight

internal fun Constraints.inferredSize(): Size {
    if (!hasBoundedWidth || !hasBoundedHeight) return Size.Unspecified
    return Size(maxWidth.toFloat(), maxHeight.toFloat())
}

private fun Size.hasSpecifiedAndFiniteWidth() = this != Size.Unspecified && width.isFinite()
private fun Size.hasSpecifiedAndFiniteHeight() = this != Size.Unspecified && height.isFinite()
