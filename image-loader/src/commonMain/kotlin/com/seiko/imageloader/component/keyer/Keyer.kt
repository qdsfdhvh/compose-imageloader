package com.seiko.imageloader.component.keyer

import com.seiko.imageloader.request.Options

fun interface Keyer<T: Any> {
    fun key(data: T, options: Options): String?

    fun tryKey(data: Any, options: Options): String? {
        @Suppress("UNCHECKED_CAST")
        return (data as? T)?.let { key(it, options) }
    }
}