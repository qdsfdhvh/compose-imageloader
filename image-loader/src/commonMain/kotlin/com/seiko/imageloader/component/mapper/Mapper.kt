package com.seiko.imageloader.component.mapper

import com.seiko.imageloader.request.Options

fun interface Mapper<V : Any> {
    fun map(data: Any, options: Options): V?
}
