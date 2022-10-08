package com.seiko.imageloader.request

import androidx.compose.runtime.Immutable
import com.seiko.imageloader.size.Scale

@Immutable
class ImageRequest internal constructor(
    val data: Any,
    val scale: Scale?,
    val options: Options?
) {
    fun newBuilder() = ImageRequestBuilder(this)
}

class ImageRequestBuilder {

    private var data: Any?
    private var scale: Scale?
    private var options: Options?

    constructor() {
        data = null
        scale = null
        options = null
    }

    constructor(request: ImageRequest) {
        data = request.data
        scale = request.scale
        options = request.options
    }

    fun data(data: Any?) = apply {
        this.data = data
    }

    fun scale(scale: Scale) = apply {
        this.scale = scale
    }

    fun options(options: Options?) = apply {
        this.options = options
    }

    fun build() = ImageRequest(
        data = data ?: NullRequestData,
        scale = scale,
        options = options,
    )
}
