package com.seiko.imageloader.model

typealias ExtraData = Map<String, Any>
typealias ExtraDataBuilder = MutableMap<String, Any>

val EmptyExtraData get() = emptyMap<String, Any>()

private const val KEY_MIME_TYPE = "KEY_MIME_TYPE"
private const val KEY_META_DATA = "KEY_META_DATA"

fun extraData(block: ExtraDataBuilder.() -> Unit) = buildMap(block)

internal fun ExtraDataBuilder.mimeType(type: String?) {
    if (!type.isNullOrEmpty()) set(KEY_MIME_TYPE, type)
}

internal inline val ExtraData.mimeType: String?
    get() = get(KEY_MIME_TYPE) as? String

internal fun ExtraDataBuilder.metadata(metadata: Any?) {
    if (metadata != null) set(KEY_META_DATA, metadata)
}

internal inline val ExtraData.metadata: Any?
    get() = get(KEY_META_DATA)
