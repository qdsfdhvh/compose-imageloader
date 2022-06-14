package com.seiko.imageloader.component.mapper

import com.seiko.imageloader.request.Options

fun interface Mapper<T : Any, V : Any> {
    fun map(data: T, options: Options): V?

    fun tryMap(data: Any, options: Options): V? {
        @Suppress("UNCHECKED_CAST")
        return (data as? T)?.let { map(it, options) }
    }
}