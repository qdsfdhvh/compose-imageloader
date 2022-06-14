package com.seiko.imageloader.component.keyer

import com.seiko.imageloader.request.Options

fun interface Keyer {
    fun key(data: Any, options: Options): String?
}