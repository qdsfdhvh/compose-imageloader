package com.seiko.imageloader.request

import androidx.compose.runtime.Immutable
import com.seiko.imageloader.size.Precision
import com.seiko.imageloader.size.Scale
import com.seiko.imageloader.size.SizeResolver

@Immutable
class ImageRequest internal constructor(
    val data: Any,
    val sizeResolver: SizeResolver?,
    val scale: Scale?,
    val precision: Precision,
) {
    fun newBuilder() = ImageRequestBuilder(this)
}

class ImageRequestBuilder {

    private var data: Any?
    private var sizeResolver: SizeResolver?
    private var scale: Scale?
    private var precision: Precision

    constructor() {
        data = null
        sizeResolver = null
        scale = null
        precision = Precision.AUTOMATIC
    }

    constructor(request: ImageRequest) {
        data = request.data
        sizeResolver = request.sizeResolver
        scale = request.scale
        precision = request.precision
    }

    fun data(data: Any?) = apply {
        this.data = data
    }

    fun size(sizeResolver: SizeResolver) = apply {
        this.sizeResolver = sizeResolver
    }

    fun scale(scale: Scale) = apply {
        this.scale = scale
    }

    fun precision(precision: Precision) = apply {
        this.precision = precision
    }

    fun build() = ImageRequest(
        data = data ?: NullRequestData,
        sizeResolver = sizeResolver,
        scale = scale,
        precision = precision,
    )
}
