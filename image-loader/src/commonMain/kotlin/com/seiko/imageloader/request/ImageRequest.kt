package com.seiko.imageloader.request

import androidx.compose.runtime.Immutable
import com.seiko.imageloader.size.Precision
import com.seiko.imageloader.size.Scale

@Immutable
class ImageRequest internal constructor(
    val data: Any,
    val scale: Scale?,
    val precision: Precision,
) {
    fun newBuilder() = ImageRequestBuilder(this)
}

class ImageRequestBuilder {

    private var data: Any?
    private var scale: Scale?
    private var precision: Precision

    constructor() {
        data = null
        scale = null
        precision = Precision.AUTOMATIC
    }

    constructor(request: ImageRequest) {
        data = request.data
        scale = request.scale
        precision = request.precision
    }

    fun data(data: Any?) = apply {
        this.data = data
    }

    fun scale(scale: Scale) = apply {
        this.scale = scale
    }

    fun precision(precision: Precision) = apply {
        this.precision = precision
    }

    fun build() = ImageRequest(
        data = data ?: NullRequestData,
        scale = scale,
        precision = precision,
    )
}
