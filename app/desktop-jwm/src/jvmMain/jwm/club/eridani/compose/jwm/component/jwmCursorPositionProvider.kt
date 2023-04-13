package club.eridani.compose.jwm.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.PopupPositionProvider
import club.eridani.compose.jwm.LocalApplicationWindow

@Composable
fun rememberJWMCursorPositionProvider(
    offset: DpOffset = DpOffset.Zero,
    alignment: Alignment = Alignment.BottomEnd,
    windowMargin: Dp = 4.dp
): PopupPositionProvider = with(LocalDensity.current) {
    val window = LocalApplicationWindow.current
    val cursorPoint = remember {
        val (x, y) = window.currentMousePos()
        IntOffset(x.toInt(), y.toInt())
    }
    val offsetPx = IntOffset(offset.x.roundToPx(), offset.y.roundToPx())
    val windowMarginPx = windowMargin.roundToPx()
    object : PopupPositionProvider {
        override fun calculatePosition(
            anchorBounds: IntRect,
            windowSize: IntSize,
            layoutDirection: LayoutDirection,
            popupContentSize: IntSize
        ) = with(density) {
            val anchor = IntRect(cursorPoint, IntSize.Zero)
            val tooltipArea = IntRect(
                IntOffset(
                    anchor.left - popupContentSize.width,
                    anchor.top - popupContentSize.height,
                ),
                IntSize(
                    popupContentSize.width * 2,
                    popupContentSize.height * 2
                )
            )
            val position = alignment.align(popupContentSize, tooltipArea.size, layoutDirection)
            var x = tooltipArea.left + position.x + offsetPx.x
            var y = tooltipArea.top + position.y + offsetPx.y
            if (x + popupContentSize.width > windowSize.width - windowMarginPx) {
                x -= popupContentSize.width
            }
            if (y + popupContentSize.height > windowSize.height - windowMarginPx) {
                y -= popupContentSize.height + anchor.height
            }
            if (x < windowMarginPx) {
                x = windowMarginPx
            }
            if (y < windowMarginPx) {
                y = windowMarginPx
            }
            IntOffset(x, y)
        }
    }
}