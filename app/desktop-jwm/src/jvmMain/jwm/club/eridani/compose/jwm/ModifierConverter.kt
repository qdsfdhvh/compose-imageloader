package club.eridani.compose.jwm

import io.github.humbleui.jwm.KeyModifier
import java.awt.event.InputEvent.*

fun jwmModifier2awt(modifier: Int): Int {
    var m = 0


    if (modifier and KeyModifier.CONTROL._mask != 0) {
        m = m or CTRL_DOWN_MASK
    }

    if (modifier and KeyModifier.SHIFT._mask != 0) {
        m = m or SHIFT_DOWN_MASK
    }

    if (modifier and KeyModifier.ALT._mask != 0) {
        m = m or ALT_DOWN_MASK
    }

    if (modifier and KeyModifier.MAC_OPTION._mask != 0) {
        m = m or ALT_DOWN_MASK
    }



    if (modifier and KeyModifier.LINUX_META._mask != 0) {
        m = m or SHIFT_DOWN_MASK
    }

    if (modifier and KeyModifier.MAC_COMMAND._mask != 0) {
        m = m or SHIFT_DOWN_MASK
    }


    if (modifier and KeyModifier.LINUX_SUPER._mask != 0) {
        m = m or META_DOWN_MASK
    }


//    if (modifier and KeyModifier.MAC_FN._mask != 0) {
//        m = m or META_DOWN_MASK
//    }

    if (modifier and KeyModifier.WIN_LOGO._mask != 0) {
        m = m or META_DOWN_MASK
    }



    return m
}