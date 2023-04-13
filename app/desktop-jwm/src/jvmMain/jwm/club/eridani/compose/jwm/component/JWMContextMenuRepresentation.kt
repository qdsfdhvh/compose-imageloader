@file:OptIn(ExperimentalComposeUiApi::class)

package club.eridani.compose.jwm.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup


val LightJWMContextMenuRepresentation = JWMContextMenuRepresentation(
    backgroundColor = Color.White,
    textColor = Color.Black,
    itemHoverColor = Color.Black.copy(alpha = 0.04f)
)

val DarkJWMContextMenuRepresentation = JWMContextMenuRepresentation(
    backgroundColor = Color(0xFF121212), // like surface in darkColors
    textColor = Color.White,
    itemHoverColor = Color.White.copy(alpha = 0.04f)
)

class JWMContextMenuRepresentation(
    private val backgroundColor: Color,
    private val textColor: Color,
    private val itemHoverColor: Color
) : ContextMenuRepresentation {

    @Composable
    override fun Representation(state: ContextMenuState, items: () -> List<ContextMenuItem>) {
        val isOpen = state.status is ContextMenuState.Status.Open
        if (isOpen) {
            Popup(
                focusable = true,
                onDismissRequest = { state.status = ContextMenuState.Status.Closed },
                popupPositionProvider = rememberJWMCursorPositionProvider(),
                onKeyEvent = {
                    if (it.key == Key.Escape) {
                        state.status = ContextMenuState.Status.Closed
                        true
                    } else {
                        false
                    }
                },
            ) {
                Column(
                    modifier = Modifier
                        .shadow(8.dp)
                        .background(backgroundColor)
                        .padding(vertical = 4.dp)
                        .width(IntrinsicSize.Max)
                        .verticalScroll(rememberScrollState())

                ) {
                    remember { items() }.distinctBy { it.label }.forEach { item ->
                        MenuItemContent(
                            itemHoverColor = itemHoverColor,
                            onClick = {
                                state.status = ContextMenuState.Status.Closed
                                item.onClick()
                            }
                        ) {
                            BasicText(text = item.label, style = TextStyle(color = textColor))
                        }
                    }
                }
            }
        }
    }

}

@Composable
private fun MenuItemContent(
    itemHoverColor: Color,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    var hovered by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .clickable(
                onClick = onClick,
            )
            .onHover { hovered = it }
            .background(if (hovered) itemHoverColor else Color.Transparent)
            .fillMaxWidth()
            // Preferred min and max width used during the intrinsic measurement.
            .sizeIn(
                minWidth = 112.dp,
                maxWidth = 280.dp,
                minHeight = 32.dp
            )
            .padding(
                PaddingValues(
                    horizontal = 16.dp,
                    vertical = 0.dp
                )
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}

private fun Modifier.onHover(onHover: (Boolean) -> Unit) = pointerInput(Unit) {
    awaitPointerEventScope {
        while (true) {
            val event = awaitPointerEvent()
            when (event.type) {
                PointerEventType.Enter -> onHover(true)
                PointerEventType.Exit -> onHover(false)
            }
        }
    }
}