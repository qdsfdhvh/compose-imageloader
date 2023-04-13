package club.eridani.compose.jwm

import java.awt.Component
import java.awt.event.KeyEvent

class JWMAWTKeyEvent(
    source: Component,
    action: Int,
    time: Long,
    modifiers: Int,
    keyCode: Int,
    char: Char = 0.toChar(),
    location: Int
) : KeyEvent(source, action, time, modifiers, keyCode, char, location)