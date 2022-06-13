package com.seiko.imageloader.component.keyer

import com.seiko.imageloader.request.Options

fun interface Keyer<T: Any> {
    fun key(data: T, options: Options): String?
}