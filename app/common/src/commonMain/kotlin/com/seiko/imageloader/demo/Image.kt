package com.seiko.imageloader.demo

import kotlinx.serialization.Serializable

@Serializable
data class Image(
    val urls: Urls? = null,
    val color: String? = "",
    val width: Int,
    val height: Int,
    val url: String = "",
) {
    @Serializable
    data class Urls(
        val raw: String,
        val full: String,
        val regular: String,
        val small: String,
        val thumb: String,
    )

    val imageUrl: String get() = urls?.small ?: url
}
