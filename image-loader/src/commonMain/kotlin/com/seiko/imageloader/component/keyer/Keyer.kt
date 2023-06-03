package com.seiko.imageloader.component.keyer

import com.seiko.imageloader.option.Options

fun interface Keyer {

    fun key(data: Any, options: Options, type: Type): String?

    enum class Type {
        Memory,
        Disk,
    }
}
