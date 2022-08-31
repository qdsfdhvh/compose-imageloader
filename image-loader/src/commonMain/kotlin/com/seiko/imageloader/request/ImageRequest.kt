package com.seiko.imageloader.request

import androidx.compose.runtime.Immutable
import com.seiko.imageloader.size.Scale

@Immutable
class ImageRequest internal constructor(
    val data: Any,
    val scale: Scale?,
) {
    fun newBuilder() = ImageRequestBuilder(this)
}

class ImageRequestBuilder {

    private var data: Any?
    private var scale: Scale?

    constructor() {
        data = null
        scale = null
    }

    constructor(request: ImageRequest) {
        data = request.data
        scale = request.scale
    }

    fun data(data: Any?) = apply {
        this.data = data
    }

    fun scale(scale: Scale) = apply {
        this.scale = scale
    }

    fun build() = ImageRequest(
        data = data ?: NullRequestData,
        scale = scale,
    )
}
