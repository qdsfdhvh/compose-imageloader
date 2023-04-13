@file:OptIn(ExperimentalFoundationApi::class)

package club.eridani.compose.jwm.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import club.eridani.compose.jwm.LocalApplicationWindow
import io.github.humbleui.jwm.TextInputClient
import io.github.humbleui.types.IRange
import io.github.humbleui.types.IRect


fun Modifier.textInputFocusable(): Modifier {
    return composed {
        val pos = remember { mutableStateOf<LayoutCoordinates?>(null) }

        val window = LocalApplicationWindow.current.window
        onGloballyPositioned {
            pos.value = it
        }.onFocusChanged {
            if (it.isFocused) {
                window.setTextInputEnabled(true)
                pos.value ?: return@onFocusChanged
                val (x, y) = (pos.value!!.positionInWindow())
                val (w, h) = (pos.value!!.size)
                window.setTextInputClient(object : TextInputClient {
                    override fun getRectForMarkedRange(
                        selectionStart: Int,
                        selectionEnd: Int
                    ): IRect {
                        return IRect.makeXYWH(x.toInt(), y.toInt(), w, h)
                    }

                    override fun getSelectedRange(): IRange {
                        TODO("Not yet implemented")
                    }

                    override fun getMarkedRange(): IRange {
                        TODO("Not yet implemented")
                    }

                    override fun getSubstring(start: Int, end: Int): String {
                        TODO("Not yet implemented")
                    }

                })
            } else {
                window.setTextInputEnabled(false)
            }
        }
    }
}