package com.seiko.imageloader.model

fun ImageRequestBuilder.ninePatch(block: NinePatchDataBuilder.() -> Unit) {
    extra {
        set(KEY_NINE_PATCH_DATA, NinePatchData(block))
    }
}

internal val ImageRequest.ninePatchData: NinePatchData?
    get() = extra[KEY_NINE_PATCH_DATA] as? NinePatchData

private const val KEY_NINE_PATCH_DATA = "KEY_NINE_PATCH_DATA"
