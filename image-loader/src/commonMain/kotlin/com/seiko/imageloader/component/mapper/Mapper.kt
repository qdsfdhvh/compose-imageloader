package com.seiko.imageloader.component.mapper

import com.seiko.imageloader.request.Options

fun interface Mapper<T : Any, V : Any> {
    fun map(data: T, options: Options): V?
}