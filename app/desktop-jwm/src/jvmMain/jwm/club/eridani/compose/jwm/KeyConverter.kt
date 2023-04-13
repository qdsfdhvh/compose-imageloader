package club.eridani.compose.jwm

import io.github.humbleui.jwm.Key
import io.github.humbleui.jwm.Key.*
import java.awt.event.KeyEvent

fun jwmKeyToAWTKey(key: Key): Int {
    return when (key) {
        UNDEFINED -> KeyEvent.VK_UNDEFINED

        CAPS_LOCK -> KeyEvent.VK_CAPS_LOCK
        SHIFT -> KeyEvent.VK_SHIFT
        CONTROL -> KeyEvent.VK_CONTROL
        ALT -> KeyEvent.VK_ALT
        WIN_LOGO -> KeyEvent.VK_META
        LINUX_META -> KeyEvent.VK_META
        LINUX_SUPER -> KeyEvent.VK_META
        MAC_COMMAND -> KeyEvent.VK_CONTROL
        MAC_OPTION -> KeyEvent.VK_ALT


        ENTER -> KeyEvent.VK_ENTER
        BACKSPACE -> KeyEvent.VK_BACK_SPACE
        TAB -> KeyEvent.VK_TAB
        SPACE -> KeyEvent.VK_SPACE
        ESCAPE -> KeyEvent.VK_ESCAPE
        HOME -> KeyEvent.VK_HOME
        END -> KeyEvent.VK_END
        PAGE_UP -> KeyEvent.VK_PAGE_UP
        PAGE_DOWN -> KeyEvent.VK_PAGE_DOWN
        LEFT -> KeyEvent.VK_LEFT
        UP -> KeyEvent.VK_UP
        RIGHT -> KeyEvent.VK_RIGHT
        DOWN -> KeyEvent.VK_DOWN

        COMMA -> KeyEvent.VK_COMMA
        MINUS -> KeyEvent.VK_MINUS
        PERIOD -> KeyEvent.VK_PERIOD
        SLASH -> KeyEvent.VK_SLASH

        DIGIT0 -> KeyEvent.VK_0
        DIGIT1 -> KeyEvent.VK_1
        DIGIT2 -> KeyEvent.VK_2
        DIGIT3 -> KeyEvent.VK_3
        DIGIT4 -> KeyEvent.VK_4
        DIGIT5 -> KeyEvent.VK_5
        DIGIT6 -> KeyEvent.VK_6
        DIGIT7 -> KeyEvent.VK_7
        DIGIT8 -> KeyEvent.VK_8
        DIGIT9 -> KeyEvent.VK_9

        SEMICOLON -> KeyEvent.VK_SEMICOLON
        EQUALS -> KeyEvent.VK_EQUALS

        F1 -> KeyEvent.VK_F1
        F2 -> KeyEvent.VK_F2
        F3 -> KeyEvent.VK_F3
        F4 -> KeyEvent.VK_F4
        F5 -> KeyEvent.VK_F5
        F6 -> KeyEvent.VK_F6
        F7 -> KeyEvent.VK_F7
        F8 -> KeyEvent.VK_F8
        F9 -> KeyEvent.VK_F9
        F10 -> KeyEvent.VK_F10
        F11 -> KeyEvent.VK_F11
        F12 -> KeyEvent.VK_F12
        F13 -> KeyEvent.VK_F13
        F14 -> KeyEvent.VK_F14
        F15 -> KeyEvent.VK_F15
        F16 -> KeyEvent.VK_F16
        F17 -> KeyEvent.VK_F17
        F18 -> KeyEvent.VK_F18
        F19 -> KeyEvent.VK_F19
        F20 -> KeyEvent.VK_F20
        F21 -> KeyEvent.VK_F21
        F22 -> KeyEvent.VK_F22
        F23 -> KeyEvent.VK_F23
        F24 -> KeyEvent.VK_F24

        A -> KeyEvent.VK_A
        B -> KeyEvent.VK_B
        C -> KeyEvent.VK_C
        D -> KeyEvent.VK_D
        E -> KeyEvent.VK_E
        F -> KeyEvent.VK_F
        G -> KeyEvent.VK_G
        H -> KeyEvent.VK_H
        I -> KeyEvent.VK_I
        J -> KeyEvent.VK_J
        K -> KeyEvent.VK_K
        L -> KeyEvent.VK_L
        M -> KeyEvent.VK_M
        N -> KeyEvent.VK_N
        O -> KeyEvent.VK_O
        P -> KeyEvent.VK_P
        Q -> KeyEvent.VK_Q
        R -> KeyEvent.VK_R
        S -> KeyEvent.VK_S
        T -> KeyEvent.VK_T
        U -> KeyEvent.VK_U
        V -> KeyEvent.VK_V
        W -> KeyEvent.VK_W
        X -> KeyEvent.VK_X
        Y -> KeyEvent.VK_Y
        Z -> KeyEvent.VK_Z

        OPEN_BRACKET -> KeyEvent.VK_OPEN_BRACKET
        BACK_SLASH -> KeyEvent.VK_BACK_SLASH
        CLOSE_BRACKET -> KeyEvent.VK_CLOSE_BRACKET
        MULTIPLY -> KeyEvent.VK_MULTIPLY
        ADD -> KeyEvent.VK_ADD
        SEPARATOR -> KeyEvent.VK_SEPARATOR
        DELETE -> KeyEvent.VK_DELETE
        NUM_LOCK -> KeyEvent.VK_NUM_LOCK
        SCROLL_LOCK -> KeyEvent.VK_SCROLL_LOCK

        PRINTSCREEN -> KeyEvent.VK_PRINTSCREEN
        INSERT -> KeyEvent.VK_INSERT
        HELP -> KeyEvent.VK_HELP
        QUOTE -> KeyEvent.VK_QUOTE
        KANA -> KeyEvent.VK_KANA


        // TODO
        MAC_FN -> KeyEvent.VK_UNDEFINED
        VOLUME_UP -> KeyEvent.VK_UNDEFINED
        VOLUME_DOWN -> KeyEvent.VK_UNDEFINED
        MENU -> KeyEvent.VK_UNDEFINED

        else -> {
            println("Unknown key: $key")
            KeyEvent.VK_UNDEFINED
        }
    }
}